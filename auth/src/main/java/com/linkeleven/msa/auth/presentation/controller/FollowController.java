package com.linkeleven.msa.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.FollowingUsernameResponseDto;
import com.linkeleven.msa.auth.application.service.FollowService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.auth.presentation.dto.FollowingUsernameRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {
	private final FollowService followService;

	@PostMapping
	public ResponseEntity<SuccessResponseDto<FollowingUsernameResponseDto>> createFollow(
		@RequestBody FollowingUsernameRequestDto requestDto,
		@RequestHeader("X-User-Id") String userId
	) {
		return ResponseEntity.ok().body(
			SuccessResponseDto.success(
				"팔로잉 신청이 되었습니다.",
				followService.createFollow(requestDto, userId)
			));
	}

}
