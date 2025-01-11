package com.linkeleven.msa.feed.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linkeleven.msa.feed.application.dto.external.CommentCountResponseDto;
import com.linkeleven.msa.feed.application.dto.external.LikeCountResponseDto;

@FeignClient(name = "interaction-service")
public interface InteractionClient {

	@GetMapping("/external/feeds/comments")
	CommentCountResponseDto getCommentCount(@RequestParam("feedIdList") List<Long> feedIdList);

	@GetMapping("/external/feeds/likes")
	LikeCountResponseDto getLikeCount(@RequestParam("feedIdList") List<Long> feedIdList);


}
