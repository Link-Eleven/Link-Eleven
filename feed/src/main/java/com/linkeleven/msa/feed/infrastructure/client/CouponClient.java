package com.linkeleven.msa.feed.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "coupon-service")
public interface CouponClient {

	@DeleteMapping("/external/coupons/{feedId}")
	void deleteCoupon(
		@PathVariable("feedId") Long feedId,
		@RequestHeader(value = "X-User-Id") Long userId,
		@RequestHeader(value = "X-Role") String role
	);

}
