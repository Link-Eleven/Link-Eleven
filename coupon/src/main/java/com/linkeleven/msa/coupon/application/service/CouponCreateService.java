package com.linkeleven.msa.coupon.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.PopularFeedResponseDto;
import com.linkeleven.msa.coupon.domain.factory.CouponPolicyFactory;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;
import com.linkeleven.msa.coupon.domain.service.CouponDomainService;
import com.linkeleven.msa.coupon.domain.service.CouponPolicyDomainService;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponCreateService {

	private final CouponRepository couponRepository;
	private final CouponPolicyDomainService couponPolicyDomainService;
	private final CouponDomainService couponDomainService;
	private final CouponRedisService couponRedisService;

	@Transactional
	public void createCoupon(PopularFeedResponseDto request) {
		checkFeedIdDuplication(request.getFeedId());
		Coupon coupon = creatingCoupon(request.getFeedId());
		List<CouponPolicy> policies = creatingCouponPolicies(coupon);
		savePoliciesToRedis(coupon.getCouponId(), policies);
	}

	private void checkFeedIdDuplication(Long feedId) {
		if (couponRepository.existsByFeedId(feedId)) {
			throw new CustomException(ErrorCode.DUPLICATE_FEED_ID);
		}
	}

	private Coupon creatingCoupon(Long feedId) {
		return couponDomainService.createCoupon(feedId);
	}

	private void savePoliciesToRedis(Long couponId, List<CouponPolicy> policies) {
		couponRedisService.savePolicies(couponId, policies);
	}

	private List<CouponPolicy> creatingCouponPolicies(Coupon coupon) {
		List<CouponPolicy> policies = CouponPolicyFactory.createPolicies(coupon.getCouponId());
		return couponPolicyDomainService.savePolicies(policies);
	}
}