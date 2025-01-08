package com.linkeleven.msa.feed.infrastructure.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "interaction", url = "http://localhost:19094")
// interaction-service와 feed-service 간의 통신을 위한 URL -> 추후 수정 예정
public interface InteractionClient {

	// @GetMapping("/external/feeds/{feedId}/comments")
	// CommentCountResponseDto getCommentCount(@PathVariable Long feedId);
	//
	// @GetMapping("/external/feeds/{feedId}/likes")
	// LikeCountResponseDto getLikeCount(@PathVariable Long feedId);

	// Batch 요청을 위한 API 추가
	@GetMapping("/external/feeds/comments")
	Map<Long, Long> getCommentCounts(@RequestParam("feedIds") List<Long> feedIds);

	@GetMapping("/external/feeds/likes")
	Map<Long, Long> getLikeCounts(@RequestParam("feedIds") List<Long> feedIds);

}
