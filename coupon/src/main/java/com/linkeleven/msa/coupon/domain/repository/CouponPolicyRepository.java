package com.linkeleven.msa.coupon.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {
	@Query("SELECT p FROM CouponPolicy p WHERE p.couponId = :couponId AND p.issuedCount < p.quantity AND p.status = 'ACTIVE' ORDER BY p.createdAt ASC")
	List<CouponPolicy> findAvailablePolicies(@Param("couponId") Long couponId);
}
