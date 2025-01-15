package com.linkeleven.msa.feed.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "recommendation-service")
public interface RecommendationClient {

	@GetMapping("/external/recommendations/keywords/{userId}")
	List<String> getRecommendationKeywords(@PathVariable("userId") Long userId);
}
