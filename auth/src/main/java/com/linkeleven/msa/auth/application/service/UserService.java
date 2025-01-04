package com.linkeleven.msa.auth.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.UserDeleteResponseDto;
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
		User user=validateSameUser(Long.valueOf(headerId),userId);
		if(!isBooleanValuesDifferent(user.getIsAnonymous(), userUpdateAnonymousRequestDto.getIsAnonymous())){
			return UserUpdateAnonymousResponseDto.of(user.getUserId(),user.getIsAnonymous());
		}
		user.updateAnonymous(userUpdateAnonymousRequestDto.getIsAnonymous());
		return UserUpdateAnonymousResponseDto.of(user.getUserId(),user.getIsAnonymous());

	}

	@Transactional
	public UserUpdateCouponIssuedResponseDto updateCouponIssued(
		String headerId,
		String role,
		Long userId,
		UserUpdateCouponIssuedRequestDto userUpdateCouponIssuedRequestDto
	) {
		User user=validateSameUser(Long.valueOf(headerId),userId);
		validateUserRole(role,user.getRole());

		if(!role.equals("COMPANY")){
			throw new CustomException(ErrorCode.ONLY_COMPANY_USER);
		}
		if(!isBooleanValuesDifferent(user.getIsCouponIssued(), userUpdateCouponIssuedRequestDto.getIsCouponIssued())){
			return UserUpdateCouponIssuedResponseDto.of(user.getUserId(),user.getIsCouponIssued());
		}
		user.updateCouponIssued(userUpdateCouponIssuedRequestDto.getIsCouponIssued());
		return UserUpdateCouponIssuedResponseDto.of(user.getUserId(), user.getIsCouponIssued());
	}

	@Transactional
	public UserUpdateUsernameResponseDto updateUsername(
		String headerId,
		Long userId,
		UserUpdateUsernameRequestDto userUpdateUsernameRequestDto
	) {
		validateUsernameDuplicate(userUpdateUsernameRequestDto.getUsername());
		User user=validateSameUser(Long.valueOf(headerId),userId);

		user.updateUsername(userUpdateUsernameRequestDto.getUsername());
		return UserUpdateUsernameResponseDto.of(user.getUserId(),user.getUsername());

	}

	@Transactional
	public UserDeleteResponseDto deleteUser(String headerId, Long userId) {
		User user=validateSameUser(Long.valueOf(headerId),userId);
		user.deleteUser(user.getUserId());

		return UserDeleteResponseDto.from(user.getUserId());
	}

	private User validateUserById(Long userId) {
		User user =userRepository.findById(userId).orElseThrow(
			()->new CustomException(ErrorCode.USER_NOT_FOUND));
		if(user.getDeletedBy()!=null){
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}

	private User validateSameUser(Long headerId, Long userId) {
		if(!headerId.equals(userId)){
			throw new CustomException(ErrorCode.USER_SELF_ACCESS_ONLY);
		}
		return validateUserById(userId);
	}

	private void validateUserRole(String headerRole, UserRole role) {
		if(!role.toString().equals(headerRole)){
			throw new CustomException(ErrorCode.USER_ROLE_NOT_EQUEALS);
		}
	}
	private void validateUsernameDuplicate(String username) {
		if(userRepository.existsByUsername(username)){
			throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
		}
	}
	//2개의 boolean값이 다른경우 true 같은경우 false
	private Boolean isBooleanValuesDifferent(Boolean original,Boolean change){
		if(original){
			if(!change){
				return true;
			}
			else return false;
		}else{
			if(change){
				return true;
			}else return false;
		}
	}
}
