package com.linkeleven.msa.coupon.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponQueryService {
	private final IssuedCouponRepository issuedCouponRepository;

	@Transactional(readOnly = true)
	public List<IssuedCouponDto> getIssuedCouponsByUserId(Long userId) {
		return issuedCouponRepository.findActiveIssuedCouponsByUserId(userId)
			.stream()
			.map(IssuedCouponDto::from)
			.toList();
	}
}