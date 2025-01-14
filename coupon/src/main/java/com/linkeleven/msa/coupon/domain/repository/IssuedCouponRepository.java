package com.linkeleven.msa.coupon.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
	Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

	@Query("SELECT ic FROM IssuedCoupon ic " +
		"JOIN Coupon c ON ic.couponId = c.couponId " +
		"WHERE ic.userId = :userId " +
		"AND ic.status = 'ISSUED' ")
	List<IssuedCoupon> findActiveIssuedCouponsByUserId(@Param("userId") Long userId);

	// 파라미터 상태 제외 후 조회
	List<IssuedCoupon> findByCouponIdAndStatusNot(Long couponId, IssuedCouponStatus issuedCouponStatus);

	// 쿠폰 발급 유무 조회
	boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
