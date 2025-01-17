package com.linkeleven.msa.interaction.presentation.controller.external;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.external.CommentCountResponseDto;
import com.linkeleven.msa.interaction.application.service.CommentQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExtCommentController {

	private final CommentQueryService commentQueryService;

	@GetMapping("/external/feeds/comments")
	public CommentCountResponseDto getCommentCount(
		@RequestParam("feedIdList") List<Long> feedIdList
	) {
		return commentQueryService.getCommentCount(feedIdList);
	}
}
