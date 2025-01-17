package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.auth.domain.model.Follow;
import com.linkeleven.msa.auth.domain.model.User;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

	boolean existsByFollowerAndFollowing(User follower,User following);

	Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
