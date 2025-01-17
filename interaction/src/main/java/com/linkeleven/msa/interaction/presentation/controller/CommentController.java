package com.linkeleven.msa.interaction.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.CommentCreateResponseDto;
import com.linkeleven.msa.interaction.application.dto.CommentUpdateResponseDto;
import com.linkeleven.msa.interaction.application.service.CommentService;
import com.linkeleven.msa.interaction.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.interaction.presentation.dto.CommentCreateRequestDto;
import com.linkeleven.msa.interaction.presentation.dto.CommentUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{feedId}")
	public ResponseEntity<SuccessResponseDto<CommentCreateResponseDto>> createComment(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long feedId,
		@RequestBody CommentCreateRequestDto requestDto
	) {
		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponseDto.success("댓글 작성 완료", responseDto));
	}

	@PatchMapping("/{feedId}/{commentId}")
	public ResponseEntity<SuccessResponseDto<CommentUpdateResponseDto>> updateComment(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long feedId,
		@PathVariable Long commentId,
		@RequestBody CommentUpdateRequestDto requestDto
	) {
		CommentUpdateResponseDto responseDto = commentService.updateComment(userId, feedId, commentId, requestDto);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("댓글 수정 완료", responseDto));
	}

	@DeleteMapping("/{feedId}/{commentId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteComment(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long feedId,
		@PathVariable Long commentId
	) {
		commentService.deleteComment(userId, feedId, commentId);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("댓글 삭제 완료"));
	}
}
