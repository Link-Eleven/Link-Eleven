package com.linkeleven.msa.auth.presentation.controller.follow;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@DeleteMapping("/following/{followingId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteFollowing(
		@RequestHeader("X-User-Id") String userId,
		@PathVariable Long followingId
	) {
		followService.deleteFollowing(Long.valueOf(userId), followingId);
		return ResponseEntity.ok().body(
			SuccessResponseDto.success("팔로잉이 삭제되었습니다.")
		);
	}

	@DeleteMapping("/follower/{followerId}")
	public ResponseEntity<SuccessResponseDto<Void>> deleteFollower(
		@RequestHeader("X-User-Id") String userId,
		@PathVariable Long followerId
	) {
		followService.deleteFollower(followerId, Long.valueOf(userId));
		return ResponseEntity.ok().body(
			SuccessResponseDto.success("팔로워가 삭제되었습니다.")
		);
	}
}
