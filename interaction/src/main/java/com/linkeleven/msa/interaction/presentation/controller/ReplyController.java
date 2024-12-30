package com.linkeleven.msa.interaction.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.interaction.application.dto.ReplyCreateResponseDto;
import com.linkeleven.msa.interaction.application.service.ReplyService;
import com.linkeleven.msa.interaction.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.interaction.presentation.dto.ReplyRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping("/{commentId}")
	public ResponseEntity<SuccessResponseDto<ReplyCreateResponseDto>> createReply(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long commentId,
		@RequestBody ReplyRequestDto requestDto
	) {
		ReplyCreateResponseDto responseDto = replyService.createReply(userId, commentId, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponseDto.success("대댓글 작성 완료", responseDto));
	}
}
