package com.linkeleven.msa.coupon.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;

public interface CouponQueryRepository {
	Page<CouponSearchResponseDto> findCouponsByFilter(
		Long userId, CouponPolicyStatus status, Long feedId, String validFrom, String validTo, Pageable pageable);
}
