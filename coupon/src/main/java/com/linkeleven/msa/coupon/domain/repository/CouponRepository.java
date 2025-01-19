package com.linkeleven.msa.coupon.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;

public interface CouponRepository {

	boolean existsByFeedId(Long feedId);

	List<Coupon> findExpiredCoupons(LocalDateTime currentTime);

	Optional<Coupon> findByFeedId(Long feedId);

	Optional<Coupon> findByCouponIdAndCreatedBy(Long couponId, Long createdBy);

	Page<CouponSearchResponseDto> findCouponsByFilter(
		Long userId, CouponPolicyStatus status, Long feedId, String validFrom, String validTo, Pageable pageable);

	Coupon save(Coupon coupon);

	Optional<Coupon> findById(Long couponId);
}
