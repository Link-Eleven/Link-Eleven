package com.linkeleven.msa.coupon.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;

public interface IssuedCouponRepository {
	Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

	List<IssuedCoupon> findActiveIssuedCouponsByUserId(@Param("userId") Long userId);

	List<IssuedCoupon> findByCouponIdAndStatusNot(Long couponId, IssuedCouponStatus issuedCouponStatus);

	boolean existsByUserIdAndCouponId(Long userId, Long couponId);

	IssuedCoupon save(IssuedCoupon issuedCoupon);
}
