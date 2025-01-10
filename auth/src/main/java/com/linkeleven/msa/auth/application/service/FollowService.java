package com.linkeleven.msa.auth.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.FollowingUsernameResponseDto;
import com.linkeleven.msa.auth.domain.model.Follow;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.FollowRepository;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;
import com.linkeleven.msa.auth.presentation.dto.FollowingUsernameRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {
	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	@Transactional
	public FollowingUsernameResponseDto createFollow(FollowingUsernameRequestDto requestDto, String userId) {
		validateFollowingSelf(Long.valueOf(userId),requestDto.getUserId());

		User user = validateUserById(Long.valueOf(userId));
		User followingUser = validateUserById(requestDto.getUserId());

		if (validateFollowing(user, followingUser)) {
			Follow follow = validateFollowByUser(user, followingUser);
			if (follow.getDeletedAt() == null) {
				throw new CustomException(ErrorCode.ALREADY_FOLLOWING);
			} else { //following 취소 했다가 다시 following 할 경우
				follow.changeDelete();
				return FollowingUsernameResponseDto.from(followingUser.getUsername());
			}
		}

		followRepository.save(Follow.createFollow(user, followingUser));
		return FollowingUsernameResponseDto.from(followingUser.getUsername());
	}
	@Transactional
	public void deleteFollowing(Long userId, Long followingId) {
		Follow follow = deletefollow(userId, followingId);
		follow.deleteUser(userId);
	}

	@Transactional
	public void deleteFollower(Long followerId, Long userId) {
		Follow follow = deletefollow(followerId, userId);
		follow.deleteUser(userId);
	}

	private boolean validateFollowing(User user, User following) {
		return followRepository.existsByFollowerAndFollowing(user, following);
	}

	private Follow validateFollowByUser(User user, User following) {
		return followRepository.findByFollowerAndFollowing(user, following).orElseThrow(() ->
			new CustomException(ErrorCode.NOT_FOLLOWING_USER));
	}

	private User validateUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (user.getDeletedBy() != null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}

	private void validateFollowingSelf(Long followerId,Long followingId) {
		if(followerId.equals(followingId)) {
			new CustomException(ErrorCode.CANNOT_FOLLOWING_SELF);
		}
	}

	private Follow deletefollow(Long followerId, Long followingId) {
		validateFollowingSelf(followerId,followingId);

		User follwer = validateUserById(followerId);
		User following = validateUserById(followingId);

		if (!validateFollowing(follwer, following)) {
			new CustomException(ErrorCode.NOT_FOLLOWING_USER);
		}
		Follow follow = validateFollowByUser(follwer, following);
		if (follow.getDeletedAt() != null) {
			new CustomException(ErrorCode.ALREADY_UNFOLLOWING);
		}
		return follow;
	}
}
