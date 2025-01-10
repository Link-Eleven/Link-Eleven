package com.linkeleven.msa.interaction.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feed-service")
public interface FeedClient {

	// @GetMapping("/external/feeds/{feedId}")
	// boolean checkFeedExists(@PathVariable Long feedId);

	@GetMapping("/external/feeds/{feedId}/users/{userId}")
	boolean checkFeedExists(@PathVariable Long feedId, @PathVariable Long userId);
}
