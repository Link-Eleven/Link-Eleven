package com.linkeleven.msa.coupon.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;

import jakarta.persistence.LockModeType;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM CouponPolicy p WHERE p.couponId = :couponId AND p.issuedCount < p.quantity AND p.status = 'ACTIVE' ORDER BY p.discountRate DESC")
	List<CouponPolicy> findAvailablePolicies(@Param("couponId") Long couponId);

	// 스케쥴러: 유효기한 지난 쿠폰 비활성화
	@Modifying(clearAutomatically = true)
	@Query(
		"UPDATE CouponPolicy p SET p.status = :status" + " WHERE p.couponId = :couponId " + "AND p.status = 'ACTIVE'")
	void updateCouponPolicyStatusToInactive(@Param("couponId") Long couponId,
		@Param("status") CouponPolicyStatus status);

	// 쿠폰 상태 제외 후 조회
	List<CouponPolicy> findByCouponIdAndStatusNot(Long couponId, CouponPolicyStatus status);

}
