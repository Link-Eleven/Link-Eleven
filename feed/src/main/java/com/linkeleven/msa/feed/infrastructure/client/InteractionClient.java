package com.linkeleven.msa.feed.infrastructure.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "interaction")
public interface InteractionClient {
	@GetMapping("/external/feeds/comments")
	Map<Long, Integer> getCommentCounts(@RequestParam("feedIds") List<Long> feedIdList);

	@GetMapping("/external/feeds/likes")
	Map<Long, Integer> getLikeCounts(@RequestParam("feedIds") List<Long> feedIdList);

}
