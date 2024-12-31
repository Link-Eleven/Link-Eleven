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

	@PostMapping("/{targetId}")
	public ResponseEntity<SuccessResponseDto<Void>> createLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long targetId,
		@RequestBody LikeRequestDto requestDto
	) {
		ContentType contentType = requestDto.validateContentType();
		likeService.createLike(userId, targetId, contentType);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponseDto.success("좋아요."));
	}

	@DeleteMapping("/{targetId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteLike(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long targetId,
		@RequestBody LikeRequestDto requestDto
	) {
		ContentType contentType = requestDto.validateContentType();
		likeService.cancelLike(userId, targetId, contentType);
		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("좋아요 취소."));
	}

}
