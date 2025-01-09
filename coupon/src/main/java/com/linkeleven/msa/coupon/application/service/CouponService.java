package com.linkeleven.msa.coupon.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.CouponResponseDto;
import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.application.dto.PopularFeedResponseDto;
import com.linkeleven.msa.coupon.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.infrastructure.client.AuthServiceClient;
import com.linkeleven.msa.coupon.infrastructure.client.FeedServiceClient;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final CouponPolicyRepository couponPolicyRepository;
	private final IssuedCouponRepository issuedCouponRepository;
	private final CouponRedisService couponRedisService;
	private final FeedServiceClient feedServiceClient;
	private final AuthServiceClient authServiceClient;

	@Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 실행
	@Transactional
	public void updateExpiredCouponsStatus() {
		// 유효기한 지난 쿠폰 상태변경
		LocalDateTime currentTime = LocalDateTime.now();
		List<Coupon> expiredCoupons = couponRepository.findExpiredCoupons(currentTime);

		for (Coupon coupon : expiredCoupons) {
			couponPolicyRepository.updateCouponPolicyStatusToInactive(coupon.getCouponId());
		}
	}

	@Scheduled(cron = "50 23 * * * ?")
	public void generateCouponsForPopularPosts() {
		// 인기 게시글 목록 - Feign Client 호출
		List<PopularFeedResponseDto> popularFeeds = feedServiceClient.getPopularFeeds();

		// UserId 추출 -> 리스트
		List<Long> userIds = popularFeeds.stream()
			.map(PopularFeedResponseDto::getUserId)
			.toList();

		// 유저 권한 불러오기
		List<UserRoleResponseDto> userRoles = authServiceClient.getUserRoles(userIds);

		// 권한 검증 후 쿠폰 생성
		userRoles.forEach(userRole -> {
			if ("COMPANY".equals(userRole.getRole())) {
				popularFeeds.stream()
					.filter(feed -> feed.getUserId().equals(userRole.getUserId()))
					.findFirst()
					.ifPresent(this::createCoupon);
			}
		});
	}

	// 쿠폰 생성
	@Transactional
	public void createCoupon(PopularFeedResponseDto request) {
		boolean exists = couponRepository.existsByFeedId(request.getFeedId());
		// 쿠폰 중복 생성 방지
		if (exists) {
			throw new CustomException(ErrorCode.DUPLICATE_FEED_ID);
		}
		// 쿠폰 유효기간 설정
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = now.toLocalDate().atStartOfDay().plusDays(1);
		LocalDateTime expiryDate = startDate.plusDays(30).minusSeconds(1);

		Coupon coupon = Coupon.of(request.getFeedId(), startDate, expiryDate);
		couponRepository.save(coupon);

		// 쿠폰 정책 생성
		List<CouponPolicy> policies = List.of(
			CouponPolicy.of(coupon.getCouponId(), 40, 5), // 40% 쿠폰 5장
			CouponPolicy.of(coupon.getCouponId(), 30, 20), // 30% 쿠폰 20장
			CouponPolicy.of(coupon.getCouponId(), 20, 75) // 20% 쿠폰 75장
		);
		couponPolicyRepository.saveAll(policies);

		// Redis에 정책별 쿠폰 저장
		policies.forEach(policy ->
			couponRedisService.addCouponsToRedis(coupon.getCouponId(), policy.getPolicyId(), policy.getQuantity())
		);
	}

	// 쿠폰 ID로 쿠폰 조회
	@Transactional(readOnly = true)
	public CouponResponseDto getCouponById(Long userId, String role, Long couponId) {
		if ("MASTER".equals(role)) {
			Coupon coupon = couponRepository.findById(couponId)
				.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
			return CouponResponseDto.from(coupon);
		} else if ("COMPANY".equals(role)) {
			Coupon coupon = couponRepository.findByCouponIdAndCreatedBy(couponId, userId)
				.orElseThrow(() -> new CustomException(ErrorCode.FORBIDDEN));
			return CouponResponseDto.from(coupon);
		}
		throw new CustomException(ErrorCode.FORBIDDEN);
	}

	// 모든 쿠폰 조회
	@Transactional(readOnly = true)
	public Page<CouponSearchResponseDto> searchCoupons(Long userId, String role, CouponPolicyStatus status, Long feedId,
		String validFrom,
		String validTo,
		Pageable pageable) {
		if ("MASTER".equals(role)) {
			return couponRepository.findCouponsByFilter(null, status, feedId, validFrom, validTo, pageable);
		} else if ("COMPANY".equals(role)) {
			return couponRepository.findCouponsByFilter(userId, status, feedId, validFrom, validTo, pageable);
		} else {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
	}

	@Transactional
	public void deleteCoupon(Long userId, String role, Long feedId) {
		if ("USER".equals(role)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		Coupon coupon = couponRepository.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		// 쿠폰 정책 삭제 처리
		List<CouponPolicy> policies = couponPolicyRepository.findByCouponIdAndStatusNot(coupon.getCouponId(),
			CouponPolicyStatus.DELETED);
		// 발급된 쿠폰 소프트 삭제 처리
		List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findByCouponIdAndStatusNot(coupon.getCouponId(),
			IssuedCouponStatus.DELETED);
		issuedCoupons.forEach(issuedCoupon -> issuedCoupon.softDelete(userId));

		// 쿠폰 및 쿠폰 정책 상태 변경 (soft delete)
		coupon.softDelete(userId);
		policies.forEach(policy -> policy.softDelete(userId));
	}
}
