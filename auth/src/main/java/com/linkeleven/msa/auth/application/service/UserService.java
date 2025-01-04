package com.linkeleven.msa.auth.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.UserMyInfoResponseDto;
import com.linkeleven.msa.auth.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateAnonymousResponseDto;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;
import com.linkeleven.msa.auth.presentation.dto.UserUpdateAnonymousRequestDto;

import jakarta.transaction.Transactional;
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

	@Transactional
	public UserUpdateAnonymousResponseDto updateAnonymous(
		String headerId,
		Long userId,
		UserUpdateAnonymousRequestDto userUpdateAnonymousRequestDto
	) {
		if(headerId.equals(userId)){
			throw new CustomException(ErrorCode.USER_SELF_ACCESS_ONLY);
		}
		User user=validateUserById(userId);
		//같은 경우 바로 return
		if(user.isAnonymous()==userUpdateAnonymousRequestDto.isAnonymous()){
			return UserUpdateAnonymousResponseDto.from(user.getUserId());
		}
		user.updateAnonymous(userUpdateAnonymousRequestDto.isAnonymous());
		return UserUpdateAnonymousResponseDto.from(user.getUserId());

	}
	private User validateUserById(long userId) {
		return userRepository.findById(userId).orElseThrow(
			()->new CustomException(ErrorCode.USER_NOT_FOUND));
	}

}
