package com.linkeleven.msa.auth.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.linkeleven.msa.auth.application.dto.KafkaUpdateUsernameDto;
import com.linkeleven.msa.auth.application.dto.UserIdAndRoleResponseDto;
import com.linkeleven.msa.auth.application.dto.UserInfoResponseDto;
import com.linkeleven.msa.auth.application.dto.UserMyInfoResponseDto;
import com.linkeleven.msa.auth.application.dto.UserRoleResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateAnonymousResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateCouponIssuedResponseDto;
import com.linkeleven.msa.auth.application.dto.UserUpdateUsernameResponseDto;
import com.linkeleven.msa.auth.application.dto.UserValidateIdResponseDto;
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
	private final CommentProduceService commentProduceService;

	public UserRoleResponseDto getUserRole(Long userId) {
		User user=validateUserById(userId);
		return UserRoleResponseDto.from(user.getRole().toString());
	}
	public UserInfoResponseDto getUsername(Long userId) {
		User user=validateUserById(userId);
		return UserInfoResponseDto.from(user.getUsername());
	}
	public List<UserIdAndRoleResponseDto> getUserRoleList(List<Long> userIdList) {
		List<UserIdAndRoleResponseDto> responseDtoList =new ArrayList<>();

		List<User> userList = userRepository.findAllUserInId(userIdList);
		// userIdList ->인기 게시글 1, 2, 3 등 작성자
		// -> where in을 사용 매치 안되면 안나올텐데
		// 그러면 중간에 탈퇴한 사람 있으면 우짬? -> 빼고 보내주자
		Map<Long, User> userMap = userList.stream()
			.collect(Collectors.toMap(User::getUserId, Function.identity()));

		for (Long userId : userIdList) {
			if(userMap.containsKey(userId)){
				User user=userMap.get(userId);
				responseDtoList.add(UserIdAndRoleResponseDto.of(user.getUserId(),user.getRole().toString()));
			}
		}
		return responseDtoList;
	}

	public UserValidateIdResponseDto getValidateUserId(Long userId) {
		User user=validateUserById(userId);
		return UserValidateIdResponseDto.from(user.getUserId());
	}

	public UserMyInfoResponseDto getUserMyInfo(String userId) {
		User user=validateUserById(Long.parseLong(userId));
		return UserMyInfoResponseDto.from(user);
	}

	@Transactional
	public UserUpdateAnonymousResponseDto updateAnonymous(
		String headerId,
		String role,
		Long userId,
		UserUpdateAnonymousRequestDto userUpdateAnonymousRequestDto
	) {
		User user=validateSameUser(Long.valueOf(headerId),userId,role);
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
		User user=validateSameUser(Long.valueOf(headerId),userId,role);
		validateUserRole(role,user.getRole());

		if(!user.getRole().toString().equals("COMPANY")){
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
		String role,
		Long userId,
		UserUpdateUsernameRequestDto userUpdateUsernameRequestDto
	) {
		validateUsernameDuplicate(userUpdateUsernameRequestDto.getUsername());
		User user=validateSameUser(Long.valueOf(headerId),userId,role);

		user.updateUsername(userUpdateUsernameRequestDto.getUsername());

		kafkaAfterCommit("username-change",KafkaUpdateUsernameDto.of(user.getUserId(), user.getUsername()));

		return UserUpdateUsernameResponseDto.of(user.getUserId(),user.getUsername());
	}

	@Transactional
	public void deleteUser(String headerId,String role, Long userId) {
		User user=validateSameUser(Long.valueOf(headerId),userId,role);
		user.deleteUser(user.getUserId());
	}

	private User validateUserById(Long userId) {
		User user =userRepository.findById(userId).orElseThrow(
			()->new CustomException(ErrorCode.USER_NOT_FOUND));
		if(user.getDeletedBy()!=null){
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}

	private User validateSameUser(Long headerId, Long userId,String role) {
		if(role.equals("MASTER")){
			return validateUserMaster(headerId,userId);
		}
		if(!headerId.equals(userId)){
			throw new CustomException(ErrorCode.USER_SELF_ACCESS_ONLY);
		}
		return validateUserById(userId);
	}

	private User validateUserMaster(Long headerId, Long userId) {
		User master=validateUserById(headerId);
		if(!master.getRole().toString().equals("MASTER")){
			throw new CustomException(ErrorCode.USER_ROLE_NOT_EQUEALS);
		}
		return validateUserById(userId);
	}

	private void validateUserRole(String headerRole, UserRole role) {
		if(!role.toString().equals("MASTER")){
			if (!role.toString().equals(headerRole)) {
				throw new CustomException(ErrorCode.USER_ROLE_NOT_EQUEALS);
			}
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
			if(!original.equals(change)){
				return true;
			}
			else return false;
		}else{
			if(change){
				return true;
			}else return false;
		}
	}

	private void kafkaAfterCommit(String topic, KafkaUpdateUsernameDto dto) {
		TransactionSynchronizationManager.registerSynchronization(
			new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					commentProduceService.sendMessage("username-change", dto);
				}
			}
		);
	}


}
