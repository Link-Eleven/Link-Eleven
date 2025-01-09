package com.linkeleven.msa.auth.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;

public interface UserQueryRepository {
	Slice<UserQueryResponseDto> findUserByUsername(String username, Pageable pageable);
}
