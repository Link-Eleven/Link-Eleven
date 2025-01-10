package com.linkeleven.msa.auth.infrastructure.repository;

import static com.linkeleven.msa.auth.domain.model.QFollow.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.FollowingQueryResponseDto;

import com.linkeleven.msa.auth.domain.model.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepositoryImpl implements FollowQueryRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<FollowingQueryResponseDto> findMyFollowByUsername(
		User user,
		String username,
		Pageable pageable,
		boolean isFollowing
	) {
		List<FollowingQueryResponseDto> followings = queryFactory
			.select(Projections.constructor(FollowingQueryResponseDto.class,
				isFollowing?follow.following.username:follow.follower.username
			))
			.from(follow)
			.where(searchFollowingCondition(user, username, isFollowing))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = followings.size() > pageable.getPageSize();
		if (hasNext) {
			followings.remove(followings.size() - 1);
		}

		return new SliceImpl<>(followings, pageable, hasNext);
	}

	private BooleanExpression searchFollowingCondition(User user, String username, boolean isFollowing) {
		BooleanExpression isNotDeleted = follow.deletedBy.isNull();
		//isFollowing -> true :user = follower (유저의 팔로잉 목록)
		//isFollowing -> false :user = following (유저의 팔로워 목록)
		BooleanExpression isMyFollow = isFollowing ? follow.follower.eq(user) : follow.following.eq(user);

		if (username == null || username.isBlank()) {
			return isMyFollow.and(isNotDeleted);
		}

		BooleanExpression isResult = isFollowing
			? follow.following.username.containsIgnoreCase(username)
			: follow.follower.username.containsIgnoreCase(username);

		return isResult.and(isMyFollow).and(isNotDeleted);
	}
}
