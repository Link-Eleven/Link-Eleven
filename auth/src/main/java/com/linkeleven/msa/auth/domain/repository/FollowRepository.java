package com.linkeleven.msa.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.FollowingQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.Follow;
import com.linkeleven.msa.auth.domain.model.User;

public interface FollowRepository {
	Follow save(Follow follow);

	Optional<Follow> findById(Long followId);

	boolean existsByFollowerAndFollowing(User follower,User following);

	Optional<Follow> findByFollowerAndFollowing(User follower,User following);

	Slice<FollowingQueryResponseDto> findMyFollowByUsername(User user,String username, Pageable pageable,boolean isFollowing);
}
