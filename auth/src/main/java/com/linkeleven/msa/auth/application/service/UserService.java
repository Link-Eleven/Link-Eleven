package com.linkeleven.msa.auth.application.service;

import org.springframework.stereotype.Service;

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
		User user=userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
		return UserRoleResponseDto.from(user.getRole().toString());
	}
}
