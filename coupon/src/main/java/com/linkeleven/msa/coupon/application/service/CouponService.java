package com.linkeleven.msa.coupon.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.CouponResponseDto;
import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.application.dto.PopularFeedResponseDto;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final CouponPolicyRepository couponPolicyRepository;
	private final IssuedCouponRepository issuedCouponRepository;
	private final CouponRedisService couponRedisService;

	@Transactional
	public void createCoupon(PopularFeedResponseDto request) {
		// 중복 쿠폰 생성 방지
		checkFeedIdDuplication(request.getFeedId());
		// 쿠폰 생성
		Coupon coupon = creatingCoupon(request);

		// 쿠폰 정책 생성
		List<CouponPolicy> policies = creatingCouponPolicies(coupon);

		// Redis에 정책별 쿠폰 저장
		savePoliciesToRedis(coupon.getCouponId(), policies);
	}

	@Transactional(readOnly = true)
	public CouponResponseDto getCouponById(Long userId, String role, Long couponId) {
		Coupon coupon;

		if ("MASTER".equals(role)) {
			// "MASTER"일 경우, 쿠폰 ID로 조회
			coupon = couponRepository.findById(couponId)
				.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
		} else if ("COMPANY".equals(role)) {
			// "COMPANY"일 경우, 유저ID와 쿠폰ID로 조회
			coupon = couponRepository.findByCouponIdAndCreatedBy(couponId, userId)
				.orElseThrow(() -> new CustomException(ErrorCode.FORBIDDEN));
		} else {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		return CouponResponseDto.from(coupon);
	}

	@Transactional(readOnly = true)
	public Page<CouponSearchResponseDto> searchCoupons(Long userId, String role, CouponPolicyStatus status,
		Long feedId, String validFrom, String validTo, Pageable pageable
	) {
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
		// 권한 체크
		validateRoleForDeletion(role);
		// 쿠폰 조회
		Coupon coupon = couponRepository.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
		// 소프트 삭제
		softDeleteCouponAndRelatedData(userId, coupon);
	}

	private void checkFeedIdDuplication(Long feedId) {
		if (couponRepository.existsByFeedId(feedId)) {
			throw new CustomException(ErrorCode.DUPLICATE_FEED_ID);
		}
	}

	private void savePoliciesToRedis(Long couponId, List<CouponPolicy> policies) {
		policies.forEach(policy ->
			couponRedisService.addCouponsToRedis(couponId, policy.getPolicyId(), policy.getQuantity(),
				policy.getDiscountRate())
		);
	}

	private Coupon creatingCoupon(PopularFeedResponseDto request) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = now.toLocalDate().atStartOfDay().plusDays(1);
		LocalDateTime expiryDate = startDate.plusDays(30).minusSeconds(1);

		Coupon coupon = Coupon.of(request.getFeedId(), startDate, expiryDate);
		coupon.setCreatedBy(request.getUserId());
		coupon.setUpdatedBy(request.getUserId());
		couponRepository.save(coupon);
		return coupon;
	}

	private List<CouponPolicy> creatingCouponPolicies(Coupon coupon) {
		List<CouponPolicy> policies = List.of(
			CouponPolicy.of(coupon.getCouponId(), 40, 5), // 40% 쿠폰 5장
			CouponPolicy.of(coupon.getCouponId(), 30, 20), // 30% 쿠폰 20장
			CouponPolicy.of(coupon.getCouponId(), 20, 75) // 20% 쿠폰 75장
		);
		couponPolicyRepository.saveAll(policies);
		return policies;
	}

	private void validateRoleForDeletion(String role) {
		if ("USER".equals(role)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
	}

	private void softDeleteCouponAndRelatedData(Long userId, Coupon coupon) {
		List<CouponPolicy> policies = couponPolicyRepository.findByCouponIdAndStatusNot(coupon.getCouponId(),
			CouponPolicyStatus.DELETED);
		List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findByCouponIdAndStatusNot(coupon.getCouponId(),
			IssuedCouponStatus.DELETED);

		issuedCoupons.forEach(issuedCoupon -> issuedCoupon.softDelete(userId));
		coupon.softDelete(userId);
		policies.forEach(policy -> policy.softDelete(userId));
	}
}
