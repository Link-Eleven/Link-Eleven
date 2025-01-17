package com.linkeleven.msa.interaction.presentation.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.ReplyQueryResponseDto;
import com.linkeleven.msa.interaction.application.service.ReplyQueryService;
import com.linkeleven.msa.interaction.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.interaction.presentation.enums.SortBy;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class ReplyQueryController {

	private final ReplyQueryService replyQueryService;

	@GetMapping("/{commentId}/replies")
	public ResponseEntity<SuccessResponseDto<Slice<ReplyQueryResponseDto>>> getReplyList(
		@PathVariable Long commentId,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(required = false) LocalDateTime cursorCreatedAt,
		@RequestParam(required = false) Long cursorLikeCount,
		@RequestParam(defaultValue = "30") @Max(50) int pageSize,
		@RequestParam(required = false) String sortBy)
	{
		String sortByEnum = String.valueOf(SortBy.fromString(sortBy));
		Slice<ReplyQueryResponseDto> slice = replyQueryService.getRepliesWithCursor(
			commentId, cursorId, cursorCreatedAt, cursorLikeCount, pageSize, sortByEnum
		);

		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("조회 성공", slice));
	}
}
