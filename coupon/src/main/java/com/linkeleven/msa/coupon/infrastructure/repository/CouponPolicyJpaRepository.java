package com.linkeleven.msa.coupon.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;

public interface CouponPolicyJpaRepository extends JpaRepository<CouponPolicy, Long> {
	@Query("SELECT p FROM CouponPolicy p WHERE p.couponId = :couponId AND p.issuedCount < p.quantity AND p.status = 'ACTIVE' ORDER BY p.discountRate DESC")
	List<CouponPolicy> findAvailablePolicies(@Param("couponId") Long couponId);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE CouponPolicy p SET p.status = :status WHERE p.couponId = :couponId AND p.status = 'ACTIVE'")
	void updateCouponPolicyStatusToInactive(@Param("couponId") Long couponId,
		@Param("status") CouponPolicyStatus status);

	List<CouponPolicy> findByCouponIdAndStatusNot(Long couponId, CouponPolicyStatus status);
}
