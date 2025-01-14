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

import com.linkeleven.msa.interaction.application.dto.ReplyCreateResponseDto;
import com.linkeleven.msa.interaction.application.dto.ReplyUpdateResponseDto;
import com.linkeleven.msa.interaction.application.service.ReplyService;
import com.linkeleven.msa.interaction.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.interaction.presentation.dto.ReplyCreateRequestDto;
import com.linkeleven.msa.interaction.presentation.dto.ReplyUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping("/{commentId}/replies")
	public ResponseEntity<SuccessResponseDto<ReplyCreateResponseDto>> createReply(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long commentId,
		@RequestBody ReplyCreateRequestDto requestDto
	) {
		ReplyCreateResponseDto responseDto = replyService.createReply(userId, commentId, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponseDto.success("대댓글 작성 완료", responseDto));
	}

	@PatchMapping("/{commentId}/replies/{replyId}")
	public ResponseEntity<SuccessResponseDto<ReplyUpdateResponseDto>> updateReply(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long replyId,
		@PathVariable Long commentId,
		@RequestBody ReplyUpdateRequestDto requestDto
	) {
		ReplyUpdateResponseDto responseDto = replyService.updateReply(userId, replyId, commentId, requestDto);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("대댓글 수정 완료", responseDto));
	}

	@DeleteMapping("/{commentId}/replies/{replyId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteReply(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long commentId,
		@PathVariable Long replyId
	) {
		replyService.deleteReply(userId, commentId, replyId);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("대댓글 삭제 완료"));
	}
}
