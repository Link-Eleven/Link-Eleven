package com.linkeleven.msa.auth.presentation.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.PageResponseDto;
import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.linkeleven.msa.auth.application.service.UserQueryService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserQueryController {
	private final UserQueryService userQueryService;

	@GetMapping
	public ResponseEntity<SuccessResponseDto<PageResponseDto<UserQueryResponseDto>>> getUsers(
		@RequestParam( required = false) String username,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Slice<UserQueryResponseDto> slice = userQueryService.getUsersByUsername(username, pageable);

		PageResponseDto<UserQueryResponseDto> responseDto = PageResponseDto.from(slice);

		return ResponseEntity.ok()
			.body(SuccessResponseDto.success("조회 성공", responseDto));
	}
}
