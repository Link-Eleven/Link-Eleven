package com.linkeleven.msa.auth.presentation.controller.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.UserInfoResponseDto;
import com.linkeleven.msa.auth.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.auth.application.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/users")
public class UserExternalController {
	private final UserService userService;

	@GetMapping("/{userId}/role")
	UserRoleResponseDto getUserRole(@PathVariable Long userId) {
		return userService.getUserRole(userId);
	}

	@GetMapping("/{userId}")
	UserInfoResponseDto getUsername(@PathVariable Long userId){
		return userService.getUsername(userId);
	}
}
