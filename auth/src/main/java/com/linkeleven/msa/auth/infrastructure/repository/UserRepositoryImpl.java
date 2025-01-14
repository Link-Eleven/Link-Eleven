package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;
	private final UserQueryRepository userQueryRepository;

	@Override
	public Optional<User> findById(Long userId) {
		return userJpaRepository.findById(userId);
	}
	@Override
	public Optional<User> findByUsername(String username) {
		return userJpaRepository.findByUsername(username);
	}
	@Override
	public boolean existsByUsername(String username) {
		return userJpaRepository.existsByUsername(username);
	}
	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public Slice<UserQueryResponseDto> findUserByUsername(String username, Pageable pageable){
		return userQueryRepository.findUserByUsername(username, pageable);
	}

	@Override
	public List<User> findAllUserInId(List<Long> userIdList){
		return userQueryRepository.findAllUserInId(userIdList);
	}

}
