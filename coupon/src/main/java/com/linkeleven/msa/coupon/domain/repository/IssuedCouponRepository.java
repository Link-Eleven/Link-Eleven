package com.linkeleven.msa.coupon.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
	boolean existsByUserIdAndCouponId(Long userId, Long couponId);

	Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
