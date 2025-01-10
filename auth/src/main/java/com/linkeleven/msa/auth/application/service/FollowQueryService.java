package com.linkeleven.msa.auth.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.FollowingQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.FollowRepository;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowQueryService {
	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	public Slice<FollowingQueryResponseDto> getFollowingList(String userId, String username, Pageable pageable) {
		User user = validateUserById(Long.valueOf(userId));
		return followRepository.findMyFollowByUsername(user, username, pageable,true); //true-> user의 following목록 조회
	}

	public Slice<FollowingQueryResponseDto> getFollowerList(String userId, String username, Pageable pageable) {
		User user = validateUserById(Long.valueOf(userId));
		return followRepository.findMyFollowByUsername(user, username, pageable,false);//false-> user의 follower목록 조회
	}

	private User validateUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (user.getDeletedBy() != null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}


}
