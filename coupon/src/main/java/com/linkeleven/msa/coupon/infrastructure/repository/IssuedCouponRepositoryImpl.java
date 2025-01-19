package com.linkeleven.msa.coupon.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

	private final IssuedCouponJpaRepository issuedCouponJpaRepository;

	@Override
	public Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId) {
		return issuedCouponJpaRepository.findByUserIdAndCouponId(userId, couponId);
	}

	@Override
	public List<IssuedCoupon> findActiveIssuedCouponsByUserId(Long userId) {
		return issuedCouponJpaRepository.findActiveIssuedCouponsByUserId(userId);
	}

	@Override
	public List<IssuedCoupon> findByCouponIdAndStatusNot(Long couponId, IssuedCouponStatus issuedCouponStatus) {
		return issuedCouponJpaRepository.findByCouponIdAndStatusNot(couponId, issuedCouponStatus);
	}

	@Override
	public boolean existsByUserIdAndCouponId(Long userId, Long couponId) {
		return issuedCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId);
	}

	@Override
	public IssuedCoupon save(IssuedCoupon issuedCoupon) {
		return issuedCouponJpaRepository.save(issuedCoupon);
	}
}