package com.linkeleven.msa.auth.infrastructure.repository;

import static com.linkeleven.msa.auth.domain.model.QUser.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<UserQueryResponseDto> findUserByUsername(String username, Pageable pageable) {
		List<UserQueryResponseDto> users = queryFactory
			.select(Projections.constructor(UserQueryResponseDto.class,
				user.userId,
				user.username,
				user.role.stringValue(),
				user.isAnonymous,
				user.isCouponIssued
			))
			.from(user)
			.where(
				username == null || username.isBlank()
					? user.deletedBy.isNull() // 삭제되지 않은 사용자만 조회
					: user.username.containsIgnoreCase(username)
					.and(user.deletedBy.isNull())
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
