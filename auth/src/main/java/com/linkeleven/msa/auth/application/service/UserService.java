package com.linkeleven.msa.auth.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.UserMyInfoResponseDto;
import com.linkeleven.msa.auth.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateAnonymousResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateCouponIssuedResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateUsernameResponseDto;
import com.linkeleven.msa.auth.domain.common.UserRole;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;
import com.linkeleven.msa.auth.presentation.dto.UserUpdateAnonymousRequestDto;
import com.linkeleven.msa.auth.presentation.dto.UserUpdateCouponIssuedRequestDto;
import com.linkeleven.msa.auth.presentation.dto.UserUpdateUsernameRequestDto;

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
		validateSameUser(Long.valueOf(headerId),userId);
		User user=validateUserById(userId);
		//같은 경우 바로 return
		if(user.isAnonymous()==userUpdateAnonymousRequestDto.isAnonymous()){
			return UserUpdateAnonymousResponseDto.from(user.getUserId());
		}
		user.updateAnonymous(userUpdateAnonymousRequestDto.isAnonymous());
		return UserUpdateAnonymousResponseDto.from(user.getUserId());

	}

	@Transactional
	public UserUpdateCouponIssuedResponseDto updateCouponIssued(
		String headerId,
		String role,
		Long userId,
		UserUpdateCouponIssuedRequestDto userUpdateCouponIssuedRequestDto
	) {
		validateSameUser(Long.valueOf(headerId),userId);
		User user=validateUserById(userId);
		validateUserRole(role,user.getRole());

		if(!role.equals("COMPANY")){
			throw new CustomException(ErrorCode.FORBIDDEN);
		}

		user.updateCouponIssued(userUpdateCouponIssuedRequestDto.isCouponIssued());
		return UserUpdateCouponIssuedResponseDto.from(user.getUserId());
	}

	@Transactional
	public UserUpdateUsernameResponseDto updateUesrname(
		String headerId,
		Long userId,
		UserUpdateUsernameRequestDto userUpdateUsernameRequestDto
	) {
		validateUsernameDuplicate(userUpdateUsernameRequestDto.getUsername());
		validateSameUser(Long.valueOf(headerId),userId);
		User user=validateUserById(userId);

		user.updateUsername(userUpdateUsernameRequestDto.getUsername());
		return UserUpdateUsernameResponseDto.from(user.getUserId());

	}



	private User validateUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			()->new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	private void validateSameUser(Long headerId, Long userId) {
		if(!headerId.equals(userId)){
			throw new CustomException(ErrorCode.USER_SELF_ACCESS_ONLY);
		}
	}

	private void validateUserRole(String headerRole, UserRole role) {
		if(!role.equals(headerRole)){
			throw new CustomException(ErrorCode.USER_ROLE_NOT_EQUEALS);
		}
	}
	private void validateUsernameDuplicate(String username) {
		if(userRepository.existsByUsername(username)){
			throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
		}
	}

}
