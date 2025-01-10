package com.linkeleven.msa.interaction.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.service.LikeService;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.interaction.presentation.dto.LikeRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;

	@PostMapping("/feeds/{feedId}")
	public ResponseEntity<SuccessResponseDto<Void>> createFeedLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long feedId,
		@RequestBody LikeRequestDto requestDto
	) {
		likeService.createLike(userId, feedId, ContentType.FEED, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponseDto.success("게시글 좋아요."));
	}

	@DeleteMapping("/feeds/{feedId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteFeedLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long feedId,
		@RequestBody LikeRequestDto requestDto
	) {
		likeService.cancelLike(userId, feedId, ContentType.FEED, requestDto);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("게시글 좋아요 취소."));
	}

	@PostMapping("/comments/{commentId}")
	public ResponseEntity<SuccessResponseDto<Void>> createCommentLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long commentId,
		@RequestBody LikeRequestDto requestDto
	) {
		likeService.createLike(userId, commentId, ContentType.COMMENT, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponseDto.success("댓글 좋아요."));
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteCommentLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long commentId,
		@RequestBody LikeRequestDto requestDto
	) {
		likeService.cancelLike(userId, commentId, ContentType.COMMENT, requestDto);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("댓글 좋아요 취소."));
	}

	@PostMapping("/replies/{replyId}")
	public ResponseEntity<SuccessResponseDto<Void>> createReplyLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long replyId,
		@RequestBody LikeRequestDto requestDto
	) {
		likeService.createLike(userId, replyId, ContentType.REPLY, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponseDto.success("대댓글 좋아요."));
	}

	@DeleteMapping("/replies/{replyId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteReplyLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long replyId,
		@RequestBody LikeRequestDto requestDto
	) {
		likeService.cancelLike(userId, replyId, ContentType.REPLY, requestDto);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("대댓글 좋아요 취소."));
	}
}
