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
import com.linkeleven.msa.coupon.application.dto.CouponWithStatsDto;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;
import com.linkeleven.msa.coupon.presentation.request.CreateCouponRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final CouponPolicyRepository couponPolicyRepository;
	private final IssuedCouponRepository issuedCouponRepository;

	@Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 실행
	@Transactional
	public void updateExpiredCouponsStatus() {
		// 유효기한 지난 쿠폰 상태변경
		LocalDateTime currentTime = LocalDateTime.now();
		List<Coupon> expiredCoupons = couponRepository.findCouponsByValidToBeforeAndCouponPolicyStatusNot(currentTime);

		for (Coupon coupon : expiredCoupons) {
			couponPolicyRepository.updateCouponPolicyStatusToInactive(coupon.getCouponId());
		}
	}

	// 쿠폰 생성
	@Transactional
	public CouponResponseDto createCoupon(Long userId, String role, CreateCouponRequestDto request) {
		// 유효한 요청인지 확인
		validateCouponRequest(request, role);

		// 쿠폰 생성
		Coupon coupon = Coupon.of(request.getFeedId(), request.getValidFrom(), request.getValidTo());
		coupon = couponRepository.save(coupon);

		// 쿠폰 정책 생성
		List<CouponPolicy> policies = List.of(
			CouponPolicy.of(coupon.getCouponId(), 40, 5), // 40% 쿠폰 5장
			CouponPolicy.of(coupon.getCouponId(), 30, 20), // 30% 쿠폰 20장
			CouponPolicy.of(coupon.getCouponId(), 20, 75) // 20% 쿠폰 75장
		);
		couponPolicyRepository.saveAll(policies);
		return CouponResponseDto.from(coupon);
	}

	// 쿠폰 ID로 쿠폰 조회
	@Transactional(readOnly = true)
	public CouponResponseDto getCouponById(Long couponId) {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
		return CouponResponseDto.from(coupon);
	}

	// 모든 쿠폰 조회
	@Transactional(readOnly = true)
	public Page<CouponSearchResponseDto> searchCoupons(CouponPolicyStatus status, Long feedId, String validFrom,
		String validTo,
		Pageable pageable) {
		if (!"MASTER".equals(role)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		return couponRepository.findCouponsByFilter(status, feedId, validFrom, validTo, pageable);
	}

	@Transactional
	public void deleteCoupon(Long userId, String role, Long feedId) {
		if ("USER".equals(role)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		Coupon coupon = couponRepository.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		List<CouponPolicy> policies = couponPolicyRepository.findByCouponIdAndStatusNot(coupon.getCouponId(),
			CouponPolicyStatus.DELETED);
		// 발급된 쿠폰 젅부 소프트 삭제 처리
		List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findByCouponIdAndStatusNot(coupon.getCouponId(),
			IssuedCouponStatus.DELETED);
		issuedCoupons.forEach(issuedCoupon -> issuedCoupon.softDelete(userId));

		// 쿠폰 및 쿠폰 정책 상태 변경 (soft delete)
		coupon.softDelete(userId);
		policies.forEach(policy -> policy.softDelete(userId));

	}

	private void validateCouponRequest(CreateCouponRequestDto request, String role) throws CustomException {
		// feed id 확인 (중복 생성 방지)
		boolean exists = couponRepository.existsByFeedId(request.getFeedId());
		if (exists) {
			throw new CustomException(ErrorCode.DUPLICATE_FEED_ID);
		}
		// 권한 확인
		if (role == null || role.equals("USER")) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		// 쿠폰 시작일 확인
		if (!(request.getValidFrom().isAfter(LocalDateTime.now())
			&& request.getValidTo().isAfter(LocalDateTime.now())
			&& request.getValidFrom().isBefore(request.getValidTo()))) {
			throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
		}

	}
}
