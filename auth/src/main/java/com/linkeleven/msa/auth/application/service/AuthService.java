package com.linkeleven.msa.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.AuthResponseDto;
import com.linkeleven.msa.auth.application.dto.AuthTokenResponseDto;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.jwt.JwtUtil;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;
import com.linkeleven.msa.auth.presentation.dto.SignInRequestDto;
import com.linkeleven.msa.auth.presentation.dto.SignUpRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public AuthResponseDto signUp(SignUpRequestDto signUpRequestDto) {
		//유저 아이디 중복 확인
		existUsername(signUpRequestDto.getUsername());

		String password = passwordEncoder.encode(signUpRequestDto.getPassword());
		User user= User.createUser(
			signUpRequestDto.getUsername(),
			password,
			signUpRequestDto.getRole(),
			signUpRequestDto.getIsAnonymous()
		);
		userRepository.save(user);

		return AuthResponseDto.from(user.getUserId());

	}

	public AuthTokenResponseDto signIn(SignInRequestDto signInRequestDto) {
		User user=validateLoginUser(signInRequestDto);

		String token=jwtUtil.createAccessToken(
			String.valueOf(user.getUserId()), user.getRole());

		return AuthTokenResponseDto.from(token);
	}



	private void existUsername(String username) {
		if(userRepository.existsByUsername(username)){
			throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
		}

	}
	private User validateLoginUser(SignInRequestDto signInRequestDto) {
		User user=userRepository.findByUsername(signInRequestDto.getUsername())
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
		if(user.getDeletedBy()!=null){
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		return user;
	}

}
