package com.linkeleven.msa.auth.presentation.controller.user;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.AuthResponseDto;
import com.linkeleven.msa.auth.application.dto.AuthTokenResponseDto;
import com.linkeleven.msa.auth.application.service.AuthService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.auth.presentation.dto.SignInRequestDto;
import com.linkeleven.msa.auth.presentation.dto.SignUpRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SuccessResponseDto<AuthResponseDto>> signUp(
		@RequestBody SignUpRequestDto signUpRequestDto)
	{
		return ResponseEntity.ok(
			SuccessResponseDto.
				success("회원가입이 되었습니다.",
					authService.signUp(signUpRequestDto))
		);
	}

	@PostMapping("/signin")
	public ResponseEntity<SuccessResponseDto<AuthTokenResponseDto>> signIn(
		@RequestBody SignInRequestDto signInRequestDto)
	{
		return ResponseEntity.ok(
			SuccessResponseDto.success("로그인 성공하였습니다.",
				authService.signIn(signInRequestDto))
		);
	}
}
