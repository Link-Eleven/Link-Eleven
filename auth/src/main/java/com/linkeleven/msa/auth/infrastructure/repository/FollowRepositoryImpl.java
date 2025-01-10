package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.FollowingQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.Follow;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {
	private final FollowJpaRepository followJpaRepository;
	private final FollowQueryRepository followQueryRepository;

	@Override
	public Follow save(Follow follow){
		return followJpaRepository.save(follow);
	}
	@Override
	public Optional<Follow> findById(Long followId) {
		return followJpaRepository.findById(followId);
	}

	@Override
	public boolean existsByFollowerAndFollowing(User follower,User following){
		return followJpaRepository.existsByFollowerAndFollowing(follower, following);
	}

	@Override
	public Optional<Follow> findByFollowerAndFollowing(User follower,User following){
		return followJpaRepository.findByFollowerAndFollowing(follower, following);
	}

	@Override
	public Slice<FollowingQueryResponseDto> findMyFollowByUsername(User user,String username, Pageable pageable,boolean isFollowing){
		return followQueryRepository.findMyFollowByUsername(user,username,pageable,isFollowing);
	}
}

