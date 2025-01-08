package com.linkeleven.msa.feed.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.linkeleven.msa.feed.application.dto.external.CommentCountResponseDto;
import com.linkeleven.msa.feed.application.dto.external.LikeCountResponseDto;

@FeignClient(name = "interaction", url = "http://localhost:19094")
// interaction-service와 feed-service 간의 통신을 위한 URL -> 추후 수정 예정
public interface InteractionClient {

	@GetMapping("/external/feeds/{feedId}/comments")
	CommentCountResponseDto getCommentCount(@PathVariable Long feedId);

	@GetMapping("/external/feeds/{feedId}/likes")
	LikeCountResponseDto getLikeCount(@PathVariable Long feedId);

}
