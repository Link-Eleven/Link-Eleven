package com.linkeleven.msa.coupon.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.infrastructure.repository.CouponRepositoryCustom;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {

	boolean existsByFeedId(Long feedId);

	// 유효기한 지난 쿠폰조회
	@Query("SELECT c FROM Coupon c JOIN c.policies cp WHERE c.validTo < :currentTime AND cp.status <> 'INACTIVE'")
	List<Coupon> findExpiredCoupons(LocalDateTime currentTime);

	// feedId로 쿠폰 조회
	Optional<Coupon> findByFeedId(Long feedId);

	Optional<Coupon> findByCouponIdAndCreatedBy(Long couponId, Long createdBy);
}
