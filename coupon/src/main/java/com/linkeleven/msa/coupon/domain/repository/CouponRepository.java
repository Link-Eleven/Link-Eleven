package com.linkeleven.msa.coupon.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.coupon.domain.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
