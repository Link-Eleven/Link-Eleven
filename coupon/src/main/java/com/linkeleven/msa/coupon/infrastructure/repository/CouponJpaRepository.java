package com.linkeleven.msa.coupon.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.linkeleven.msa.coupon.domain.model.Coupon;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
	@Query("SELECT c FROM Coupon c JOIN c.policies cp WHERE c.validTo < :currentTime AND cp.status <> 'INACTIVE'")
	List<Coupon> findExpiredCoupons(LocalDateTime currentTime);
}
