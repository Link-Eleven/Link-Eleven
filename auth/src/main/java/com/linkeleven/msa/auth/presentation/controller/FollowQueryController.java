package com.linkeleven.msa.auth.presentation.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.FollowingQueryResponseDto;
import com.linkeleven.msa.auth.application.service.FollowQueryService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowQueryController {
	private final FollowQueryService followQueryService;

	@GetMapping("/following")
	public ResponseEntity<SuccessResponseDto<Slice<FollowingQueryResponseDto>>> getFollowingList(
		@RequestHeader("X-User-Id") String userId,
		@RequestParam(required = false) String username,
		Pageable pageable
	) {
		return ResponseEntity.ok().body(SuccessResponseDto.success(
			"팔로잉 조회가 되었습니다.",
			followQueryService.getFollowingList(userId, username, pageable)
		));
	}

	@GetMapping("/follower")
	public ResponseEntity<SuccessResponseDto<Slice<FollowingQueryResponseDto>>> getFollowerList(
		@RequestHeader("X-User-Id") String userId,
		@RequestParam(required = false) String username,
		Pageable pageable
	) {
		return ResponseEntity.ok().body(SuccessResponseDto.success(
			"팔로우 조회가 되었습니다.",
			followQueryService.getFollowerList(userId, username, pageable)
		));
	}
}
