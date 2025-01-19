package com.linkeleven.msa.coupon.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponPolicyRepositoryImpl implements CouponPolicyRepository {

	private final CouponPolicyJpaRepository couponPolicyJpaRepository;

	@Override
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public List<CouponPolicy> findAvailablePolicies(Long couponId) {
		return couponPolicyJpaRepository.findAvailablePolicies(couponId);
	}

	@Override
	public void updateCouponPolicyStatusToInactive(Long couponId, CouponPolicyStatus status) {
		couponPolicyJpaRepository.updateCouponPolicyStatusToInactive(couponId, CouponPolicyStatus.INACTIVE);
	}

	@Override
	public List<CouponPolicy> findByCouponIdAndStatusNot(Long couponId, CouponPolicyStatus status) {
		return couponPolicyJpaRepository.findByCouponIdAndStatusNot(couponId, status);
	}

	@Override
	public List<CouponPolicy> saveAll(List<CouponPolicy> policies) {
		return couponPolicyJpaRepository.saveAll(policies);
	}

	@Override
	public CouponPolicy save(CouponPolicy policy) {
		return couponPolicyJpaRepository.save(policy);
	}
}