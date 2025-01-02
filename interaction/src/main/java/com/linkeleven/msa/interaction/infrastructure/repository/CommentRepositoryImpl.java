package com.linkeleven.msa.interaction.infrastructure.repository;

import static com.linkeleven.msa.interaction.domain.model.entity.QComment.*;
import static com.linkeleven.msa.interaction.domain.model.entity.QLike.*;
import static com.linkeleven.msa.interaction.domain.model.entity.QReply.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.interaction.application.dto.CommentQueryResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<CommentQueryResponseDto> findCommentByFeedWithCursor(
		Long feedId, Long cursorId, int pageSize, String sortBy,
		Long cursorLikeCount, LocalDateTime cursorCreatedAt)
	{
		return queryFactory.select(Projections.constructor(CommentQueryResponseDto.class,
				comment.id,
				comment.contentDetails.userId,
				comment.contentDetails.username,
				comment.contentDetails.content,
				comment.createdAt,
				like.count().as("likeCount"),
				reply.count().as("replyCount")
			))
			.from(comment)
			.leftJoin(like).on(like.target.targetId.eq(comment.id))
			.leftJoin(reply).on(reply.commentId.eq(comment.id))
			.where(comment.feedId.eq(feedId),
				isNotDeleted(),
				// isNotReported(),
				cursorCondition(cursorId, sortBy, cursorLikeCount, cursorCreatedAt))
			.groupBy(comment.id)
			.orderBy(getOrderSpecifier(sortBy), comment.id.desc())
			.limit(pageSize)
			.fetch();
	}

	private BooleanExpression cursorCondition(Long cursorId, String sortBy, Long cursorLikeCount, LocalDateTime cursorCreatedAt) {
		return "like".equalsIgnoreCase(sortBy) ?
			cursorLikeCondition(cursorId, cursorLikeCount) :
			cursorCreatedAtCondition(cursorId, cursorCreatedAt);
	}

	private BooleanExpression cursorLikeCondition(Long cursorId, Long cursorLikeCount) {
		if (cursorId == null || cursorLikeCount == null) {
			return null;
		}
		return like.count().lt(cursorLikeCount)
			.or(like.count().eq(cursorLikeCount).and(comment.id.gt(cursorId)));
	}

	private BooleanExpression cursorCreatedAtCondition(Long cursorId, LocalDateTime cursorCreatedAt) {
		if (cursorId == null || cursorCreatedAt == null) {
			return null;
		}
		return comment.createdAt.lt(cursorCreatedAt)
			.or(comment.createdAt.eq(cursorCreatedAt).and(comment.id.gt(cursorId)));
	}

	private OrderSpecifier<?> getOrderSpecifier(String sortBy) {
		if ("like".equalsIgnoreCase(sortBy)) {
			return like.count().desc();
		} else {
			return comment.createdAt.desc();
		}
	}

	private BooleanExpression isNotDeleted() {
		return comment.deletedAt.isNull();
	}

	// private BooleanExpression isNotReported() {
	// 	return comment.isReported.eq(false);
	// }
}
