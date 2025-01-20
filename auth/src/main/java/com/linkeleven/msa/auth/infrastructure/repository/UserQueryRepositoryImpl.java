package com.linkeleven.msa.auth.infrastructure.repository;

import static com.linkeleven.msa.auth.domain.model.QUser.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.linkeleven.msa.auth.domain.common.UserRole;
import com.linkeleven.msa.auth.domain.model.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
			.where(searchCondition(username))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = users.size() > pageable.getPageSize();
		if (hasNext) {
			users.remove(users.size() - 1);
		}

		return new SliceImpl<>(users, pageable, hasNext);
	}

	private BooleanExpression searchCondition(String username) {

		BooleanExpression isNotDeleted = user.deletedBy.isNull();

		if (username == null || username.isBlank()) {
			return isNotDeleted;
		}

		return user.username.containsIgnoreCase(username).and(isNotDeleted);
	}

	@Override
	public List<User> findAllUserInId(List<Long> userIdList) {
		return queryFactory.query()
			.select(user)
			.from(user)
			.where(
				user.deletedBy.isNull(),
				user.userId.in(userIdList),
				user.role.eq(UserRole.COMPANY),
				user.isCouponIssued.eq(true)
			)
			.fetch();
	}
}
