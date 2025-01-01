package com.linkeleven.msa.interaction.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feed-service")
public interface FeedClient {

	@GetMapping("/external/feeds/{feedId}")
	boolean checkFeedExists(@PathVariable Long feedId);

}
