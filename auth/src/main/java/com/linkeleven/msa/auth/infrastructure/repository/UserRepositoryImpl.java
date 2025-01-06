package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.QUser;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.domain.repository.UserRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository , UserRepositoryCustom {
	private final UserJpaRepository userJpaRepository;
	private final JPAQueryFactory queryFactory;

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
	public Slice<UserQueryResponseDto> findUserByUsername(String username, Pageable pageable) {
		List<UserQueryResponseDto> users = queryFactory
			.select(Projections.constructor(UserQueryResponseDto.class,
				QUser.user.userId,
				QUser.user.username,
				QUser.user.role.stringValue(),
				QUser.user.isAnonymous,
				QUser.user.isCouponIssued
			))
			.from(QUser.user)
			.where(
				username == null || username.isBlank()
					? null // username이 비어 있으면 조건 없이 모든 사용자
					: QUser.user.username.containsIgnoreCase(username) // username 포함 조건
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = users.size() > pageable.getPageSize();
		if (hasNext) {
			users.remove(users.size() - 1);
		}

		return new SliceImpl<>(users, pageable, hasNext);
	}
}
