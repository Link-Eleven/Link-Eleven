package com.linkeleven.msa.feed.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;

@FeignClient(name = "coupon-service")
public interface CouponClient {

	@PostMapping("/external/coupons")
	void createCoupons(@RequestBody List<FeedTopResponseDto> topFeeds);

	@DeleteMapping("/external/coupons/{feedId}")
	void deleteCoupons(@PathVariable("feedId") Long feedId);

}
