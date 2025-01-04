package com.linkeleven.msa.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.UserMyInfoResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateAnonymousResponseDto;
import com.linkeleven.msa.auth.application.service.UserService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.auth.presentation.dto.UserUpdateAnonymousRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<SuccessResponseDto<UserMyInfoResponseDto>> getUserMyInfo(
		@RequestHeader("X-User-Id")String userId
	){
		return ResponseEntity.ok(SuccessResponseDto.success(
			"유저 본인 정보를 조회하였습니다.",
			userService.getUserMyInfo(userId)
		));
	}
	
	@PatchMapping("/{userId}/isanonymous")
	public ResponseEntity<SuccessResponseDto<UserUpdateAnonymousResponseDto>> updateAnonymous (
		@RequestHeader("X-User-Id")String headerId,
		@PathVariable Long userId,
		@RequestBody UserUpdateAnonymousRequestDto userUpdateAnonymousRequestDto
	){
		return ResponseEntity.ok(SuccessResponseDto.success(
			"유저 익명성 정보가 변경되었습니다.",
			userService.updateAnonymous(headerId,userId,userUpdateAnonymousRequestDto)
			));
	}

}
