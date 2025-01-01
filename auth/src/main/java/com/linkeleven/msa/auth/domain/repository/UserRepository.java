package com.linkeleven.msa.auth.domain.repository;

import java.util.Optional;

import com.linkeleven.msa.auth.domain.model.User;

public interface UserRepository {

	Optional<User> findById(Long userId);
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	User save(User user);
}
