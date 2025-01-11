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

@Service
@RequiredArgsConstructor
public class CouponScheduledService {

	private final JpaAuditorAware jpaAuditorAware;
	private final CouponRepository couponRepository;
	private final CouponPolicyRepository couponPolicyRepository;
	private final FeedServiceClient feedServiceClient;
	private final AuthServiceClient authServiceClient;
	private final CouponService couponService;

	@Scheduled(cron = "0 50 23 * * ?")
	public void generateCouponsForPopularFeeds() {
		try {

			// 인기 게시글 목록 - Feign Client 호출
			List<PopularFeedResponseDto> popularFeeds = feedServiceClient.getPopularFeeds();

			// UserId 추출 -> 리스트
			List<Long> userIds = popularFeeds.stream()
				.map(PopularFeedResponseDto::getUserId)
				.toList();

			// 유저 권한 불러오기
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
								couponService.createCoupon(feed);
							} finally {
								// 처리 완료 후 ThreadLocal 정리
								jpaAuditorAware.clearCurrentAuditor();
							}
						});
				}
			});
		} catch (Exception e) {
			// 예외처리..
		}
	}

	@Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 실행
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

