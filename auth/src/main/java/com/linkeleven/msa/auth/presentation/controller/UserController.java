package com.linkeleven.msa.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.UserMyInfoResponseDto;
import com.linkeleven.msa.auth.application.service.UserService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;

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
}
