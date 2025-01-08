package com.linkeleven.msa.coupon.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.linkeleven.msa.coupon.presentation.request.CouponRequestDto;

@FeignClient(name = "feed-service")
public interface FeedServiceClient {
	@GetMapping("/external/popular-feeds")
	List<CouponRequestDto> getPopularFeeds();
}
