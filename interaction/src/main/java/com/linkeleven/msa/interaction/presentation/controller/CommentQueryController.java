package com.linkeleven.msa.interaction.presentation.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.CommentQueryResponseDto;
import com.linkeleven.msa.interaction.application.dto.PageResponseDto;
import com.linkeleven.msa.interaction.application.service.CommentQueryService;
import com.linkeleven.msa.interaction.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.interaction.presentation.enums.SortBy;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentQueryController {

	private final CommentQueryService commentQueryService;

	@GetMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<PageResponseDto<CommentQueryResponseDto>>> getCommentList(
		@PathVariable Long feedId,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(required = false) LocalDateTime cursorCreatedAt,
		@RequestParam(required = false) Long cursorLikeCount,
		@RequestParam(defaultValue = "30") @Max(50) int pageSize,
		@RequestParam(required = false) String sortBy)
	{
		String sortByEnum = String.valueOf(SortBy.fromString(sortBy));
		Slice<CommentQueryResponseDto> slice = commentQueryService.getCommentsWithCursor(
			feedId, cursorId, cursorCreatedAt, cursorLikeCount, pageSize, sortByEnum);

		PageResponseDto<CommentQueryResponseDto> responseDto = PageResponseDto.from(slice);

		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("조회 성공", responseDto));
	}
}
