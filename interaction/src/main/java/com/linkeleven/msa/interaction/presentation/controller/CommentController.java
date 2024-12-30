package com.linkeleven.msa.interaction.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	// @PostMapping("/{feedId}")
	// public ResponseEntity<SuccessResponseDto<CommentCreateResponseDto>> createComment(
	// 	@RequestHeader("X-User-Id") Long userId,
	// 	@RequestHeader(value = "Authorization") String token,
	// 	@RequestBody CommentCreateRequestDto requestDto
	// ) {
	// 	CommentCreateResponseDto responseDto = commentService.createComment(userId, token, requestDto);
	// 	return ResponseEntity.status(HttpStatus.CREATED)
	// 		.body(SuccessResponseDto.success("댓글 작성 완료", responseDto));
	// }
}
