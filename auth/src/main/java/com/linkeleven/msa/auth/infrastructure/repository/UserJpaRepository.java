package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.auth.domain.model.User;

public interface UserJpaRepository extends JpaRepository<User,Long> {
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
