package com.linkeleven.msa.coupon.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.infrastructure.repository.CouponRepositoryCustom;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {

	boolean existsByFeedId(Long feedId);

	// 유효기한 지난 쿠폰조회
	@Query("SELECT c FROM Coupon c JOIN c.policies cp WHERE c.validTo < :currentTime AND cp.status <> 'INACTIVE'")
	List<Coupon> findCouponsByValidToBeforeAndCouponPolicyStatusNot(LocalDateTime currentTime);

	// 발행된 쿠폰 수, 사용된 쿠폰 수 카운팅
	@Query("SELECT c, " +
		"(SELECT COUNT(ic) FROM IssuedCoupon ic WHERE ic.couponId = c.couponId) AS issuedCount, " +
		"(SELECT COUNT(ic) FROM IssuedCoupon ic WHERE ic.couponId = c.couponId AND ic.status = 'USED') AS usedCount " +
		"FROM Coupon c " +
		"WHERE c.feedId = :feedId")
	List<Object[]> findCouponsWithIssuedStatsByFeedId(@Param("feedId") Long feedId);

	// feedId로 쿠폰 조회
	Optional<Coupon> findByFeedId(Long feedId);

}
