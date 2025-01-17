package com.linkeleven.msa.coupon.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.PopularFeedResponseDto;
import com.linkeleven.msa.coupon.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;
import com.linkeleven.msa.coupon.infrastructure.client.AuthServiceClient;
import com.linkeleven.msa.coupon.infrastructure.client.FeedServiceClient;
import com.linkeleven.msa.coupon.infrastructure.configuration.JpaAuditorAware;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponScheduledService {

	private final JpaAuditorAware jpaAuditorAware;
	private final CouponRepository couponRepository;
	private final CouponPolicyRepository couponPolicyRepository;
	private final FeedServiceClient feedServiceClient;
	private final AuthServiceClient authServiceClient;
	private final CouponCreateService couponCreateService;

	@Scheduled(cron = "0 50 23 * * ?")
	public void generateCouponsForPopularFeeds() {
		try {
			List<PopularFeedResponseDto> popularFeeds = feedServiceClient.getPopularFeeds();

			List<Long> userIds = popularFeeds.stream()
				.map(PopularFeedResponseDto::getUserId)
				.toList();

			List<UserRoleResponseDto> userRoles = authServiceClient.getUserRoles(userIds);

			userRoles.forEach(userRole -> {
				if ("COMPANY".equals(userRole.getRole())) {
					popularFeeds.stream()
						.filter(feed -> feed.getUserId().equals(userRole.getUserId()))
						.findFirst()
						.ifPresent(feed -> {
							// 각 쿠폰 생성 전에 ThreadLocal에 사용자 ID 설정
							jpaAuditorAware.setCurrentAuditor(feed.getUserId());
							try {
								couponCreateService.createCoupon(feed);
							} finally {
								// 처리 완료 후 ThreadLocal 정리
								jpaAuditorAware.clearCurrentAuditor();
							}
						});
				}
			});
		} catch (Exception e) {
			log.error("인기 게시글 쿠폰 생성 중 예외 발생: {}", e.getMessage());
		}
	}

	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void updateExpiredCouponsStatus() {
		// 유효기한 지난 쿠폰 상태변경
		LocalDateTime currentTime = LocalDateTime.now();
		List<Coupon> expiredCoupons = couponRepository.findExpiredCoupons(currentTime);

		for (Coupon coupon : expiredCoupons) {
			couponPolicyRepository.updateCouponPolicyStatusToInactive(coupon.getCouponId(),
				CouponPolicyStatus.INACTIVE);
		}
	}
}

