package com.linkeleven.msa.coupon.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
	private final CouponQueryRepository couponQueryRepository;
	private final CouponJpaRepository couponJpaRepository;

	@Override
	public boolean existsByFeedId(Long feedId) {
		// feedId로 쿠폰이 존재하는지 확인
		return couponJpaRepository.existsById(feedId);
	}

	@Override
	public List<Coupon> findExpiredCoupons(LocalDateTime currentTime) {
		// 유효기한이 지난 쿠폰을 조회
		return couponJpaRepository.findExpiredCoupons(currentTime);
	}

	@Override
	public Optional<Coupon> findByFeedId(Long feedId) {
		// feedId로 쿠폰을 조회
		return couponJpaRepository.findById(feedId);
	}

	@Override
	public Optional<Coupon> findByCouponIdAndCreatedBy(Long couponId, Long createdBy) {
		// couponId와 createdBy로 쿠폰을 조회
		return couponJpaRepository.findById(couponId).filter(coupon -> coupon.getCreatedBy().equals(createdBy));
	}

	@Override
	public Page<CouponSearchResponseDto> findCouponsByFilter(
		Long userId, CouponPolicyStatus status, Long feedId, String validFrom, String validTo, Pageable pageable) {
		return couponQueryRepository.findCouponsByFilter(
			userId, status, feedId, validFrom, validTo, pageable);
	}

	@Override
	public Coupon save(Coupon coupon) {
		return couponJpaRepository.save(coupon);
	}

	@Override
	public Optional<Coupon> findById(Long couponId) {
		return couponJpaRepository.findById(couponId);
	}

}