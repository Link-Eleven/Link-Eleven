package com.linkeleven.msa.auth.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.UserMyInfoResponseDto;
import com.linkeleven.msa.auth.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public UserRoleResponseDto getUserRole(Long userId) {
		User user=validateUserById(userId);
		return UserRoleResponseDto.from(user.getRole().toString());
	}

	public UserMyInfoResponseDto getUserMyInfo(String userId) {
		User user=validateUserById(Long.parseLong(userId));
		return UserMyInfoResponseDto.from(user);
	}

	private User validateUserById(long userId) {
		return userRepository.findById(userId).orElseThrow(
			()->new CustomException(ErrorCode.USER_NOT_FOUND));
	}

}
