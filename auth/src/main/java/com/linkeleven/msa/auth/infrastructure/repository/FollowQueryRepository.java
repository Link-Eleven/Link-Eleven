package com.linkeleven.msa.auth.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.FollowingQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.User;

public interface FollowQueryRepository {
	Slice<FollowingQueryResponseDto> findMyFollowByUsername(User user,String username, Pageable pageable,boolean isFollowing);
}
