package com.linkeleven.msa.coupon.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.CouponResponseDto;
import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponQueryService {
	private final IssuedCouponRepository issuedCouponRepository;
	private final CouponRepository couponRepository;

	@Transactional(readOnly = true)
	public List<IssuedCouponDto> getIssuedCouponsByUserId(Long userId) {
		return issuedCouponRepository.findActiveIssuedCouponsByUserId(userId)
			.stream()
			.map(IssuedCouponDto::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public CouponResponseDto getCouponById(Long userId, String role, Long couponId) {
		Coupon coupon;

		if ("MASTER".equals(role)) {
			coupon = couponRepository.findById(couponId)
				.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
		} else if ("COMPANY".equals(role)) {
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
}