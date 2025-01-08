package com.linkeleven.msa.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.User;

public interface UserRepository {

	Optional<User> findById(Long userId);
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	User save(User user);

	Slice<UserQueryResponseDto> findUserByUsername(String username, Pageable pageable);
}
