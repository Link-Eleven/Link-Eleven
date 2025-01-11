package com.linkeleven.msa.coupon.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.infrastructure.aop.DistributedLock;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponIssuingService {
	private final CouponPolicyRepository couponPolicyRepository;
	private final IssuedCouponRepository issuedCouponRepository;
	private final CouponRedisService couponRedisService;

	/**
	 * 주어진 사용자 ID와 쿠폰 ID를 기반으로 쿠폰 발급
	 * 자정 전에는 쿠폰 발급을 막고, Redis를 활용하여 중복 발급 방지
	 * 발급 실패 시 Redis에서 중복 체크 키 정리
	 *
	 * @param userId  쿠폰을 요청하는 사용자의 ID
	 * @param role    사용자의 역할
	 * @param couponId 발급할 쿠폰의 ID
	 * @return IssuedCouponDto 발급된 쿠폰의 세부 정보
	 * @throws CustomException 쿠폰 발급이 아직 불가능하거나 이미 발급된 경우
	 */
	@Transactional
	public IssuedCouponDto issueCoupon(Long userId, String role, Long couponId) {
		// 쿠폰 발급 가능 시간 확인
		if (isBeforeMidnight()) {
			throw new CustomException(ErrorCode.COUPON_CANNOT_BE_ISSUED_YET);
		}

		if (couponRedisService.isUserCouponAlreadyIssued(userId, couponId) ||
			issuedCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
			throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
		}

		try {
			return processCouponIssuance(userId, couponId);
		} catch (Exception e) {
			couponRedisService.deleteUserCouponCheck(userId, couponId);
			throw e;
		}
	}

	/**
	 * 분산 락을 사용하여 쿠폰 발급 시도.
	 * 사용 가능한 쿠폰 정책을 검색하고, 검증한 후 쿠폰 발급
	 *
	 * @param userId   사용자의 ID
	 * @param couponId 쿠폰의 ID
	 * @return IssuedCouponDto 발급된 쿠폰의 세부 정보
	 * @throws CustomException 사용 가능한 정책이 없거나 쿠폰이 소진된 경우
	 */
	@DistributedLock(key = "'coupon_issue:' + #couponId")
	private IssuedCouponDto processCouponIssuance(Long userId, Long couponId) {

		List<CouponPolicy> availablePolicies = couponPolicyRepository.findAvailablePolicies(couponId);
		// 정책 유효성 검사
		validateAvailablePolicies(availablePolicies);

		// 쿠폰 코드 발급
		String couponCode = issueCouponCode(couponId, availablePolicies);

		// 쿠폰 정책 ID 추출
		Long issuedPolicyId = parsePolicyIdFromCouponCode(couponCode);

		// 정책 ID로 정책 찾기
		CouponPolicy selectedPolicy = findPolicyById(availablePolicies, issuedPolicyId);

		selectedPolicy.issueCoupon();
		couponPolicyRepository.save(selectedPolicy);

		IssuedCoupon issuedCoupon = IssuedCoupon.of(userId, couponId, selectedPolicy.getDiscountRate());
		issuedCoupon = issuedCouponRepository.save(issuedCoupon);

		return IssuedCouponDto.from(issuedCoupon);
	}

	/**
	 * 사용자가 발급받은 쿠폰을 사용하고 해당 쿠폰의 상태를 '사용됨(USED)'으로 변경
	 * 쿠폰이 존재하며 사용 가능한 상태인지 확인합니다.
	 *
	 * @param userId   쿠폰을 사용하는 사용자의 ID
	 * @param couponId 사용하려는 쿠폰의 ID
	 * @return IssuedCouponDto 사용된 쿠폰의 세부 정보
	 * @throws CustomException 쿠폰을 찾을 수 없거나 이미 사용된 경우
	 */
	@Transactional
	public IssuedCouponDto useCoupon(Long userId, Long couponId) {
		// 발급된 쿠폰을 조회
		IssuedCoupon issuedCoupon = findIssuedCoupon(userId, couponId);
		// 쿠폰 상태를 검증
		validateCouponStatus(issuedCoupon);

		issuedCoupon.updateStatus(IssuedCouponStatus.USED);
		return IssuedCouponDto.from(issuedCoupon);
	}

	/**
	 * 주어진 사용자 ID로 발급된 활성 쿠폰 목록 조회
	 *
	 * @param userId 사용자의 ID
	 * @return List<IssuedCouponDto> 발급된 활성 쿠폰 목록
	 */
	@Transactional(readOnly = true)
	public List<IssuedCouponDto> getIssuedCouponsByUserId(Long userId) {
		List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findActiveIssuedCouponsByUserId(userId);
		return issuedCoupons.stream()
			.map(IssuedCouponDto::from)
			.toList();
	}

	private boolean isBeforeMidnight() {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime midnight = currentTime.toLocalDate().atStartOfDay().plusDays(1);
		return currentTime.isBefore(midnight);
	}

	private void validateAvailablePolicies(List<CouponPolicy> availablePolicies) {
		if (availablePolicies.isEmpty()) {
			throw new CustomException(ErrorCode.NO_AVAILABLE_POLICY);
		}
	}

	private Long parsePolicyIdFromCouponCode(String couponCode) {
		try {
			return Long.parseLong(couponCode.split(":")[0]);
		} catch (NumberFormatException e) {
			throw new CustomException(ErrorCode.INVALID_COUPON_CODE);
		}
	}

	private CouponPolicy findPolicyById(List<CouponPolicy> availablePolicies, Long policyId) {
		return availablePolicies.stream()
			.filter(policy -> policy.getPolicyId().equals(policyId))
			.findFirst()
			.orElseThrow(() -> new CustomException(ErrorCode.NO_AVAILABLE_POLICY));
	}

	private String issueCouponCode(Long couponId, List<CouponPolicy> availablePolicies) {
		List<Long> policyIds = availablePolicies.stream()
			.map(CouponPolicy::getPolicyId)
			.collect(Collectors.toList());
		return couponRedisService.issueCouponFromRedis(couponId, policyIds)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_SOLD_OUT));
	}

	private IssuedCoupon findIssuedCoupon(Long userId, Long couponId) {
		return issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
	}

	private void validateCouponStatus(IssuedCoupon issuedCoupon) {
		if (issuedCoupon.getStatus() != IssuedCouponStatus.ISSUED) {
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}
	}
}
