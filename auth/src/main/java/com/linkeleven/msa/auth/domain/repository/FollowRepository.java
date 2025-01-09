package com.linkeleven.msa.auth.domain.repository;

import java.util.Optional;

import com.linkeleven.msa.auth.domain.model.Follow;
import com.linkeleven.msa.auth.domain.model.User;

public interface FollowRepository {
	Follow save(Follow follow);

	Optional<Follow> findById(Long followId);

	boolean existsByFollowerAndFollowing(User follower,User following);

	Optional<Follow> findByFollowerAndFollowing(User follower,User following);

}
