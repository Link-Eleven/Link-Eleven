package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.User;

public interface UserQueryRepository {
	Slice<UserQueryResponseDto> findUserByUsername(String username, Pageable pageable);
	List<User> findAllUserInId(List<Long> userIdList);
}
