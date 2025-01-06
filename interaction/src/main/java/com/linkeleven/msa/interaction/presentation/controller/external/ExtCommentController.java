package com.linkeleven.msa.interaction.presentation.controller.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.external.CommentCountResponseDto;
import com.linkeleven.msa.interaction.application.service.CommentQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/feeds/{feedId}/comments")
public class ExtCommentController {

	private final CommentQueryService commentQueryService;

	@GetMapping
	public CommentCountResponseDto getCommentCount(
		@PathVariable Long feedId
	) {
		return commentQueryService.getCommentCount(feedId);
	}
}
