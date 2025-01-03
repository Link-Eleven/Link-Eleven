package com.linkeleven.msa.interaction.infrastructure.repository;

import static com.linkeleven.msa.interaction.domain.model.entity.QLike.*;
import static com.linkeleven.msa.interaction.domain.model.entity.QReply.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.interaction.application.dto.ReplyQueryResponseDto;
import com.linkeleven.msa.interaction.domain.repository.ReplyRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<ReplyQueryResponseDto> findReplyByCommentWithCursor(
		Long commentId, Long cursorId, int pageSize,
		String sortByEnum, Long cursorLikeCount, LocalDateTime cursorCreatedAt)
	{
		List<ReplyQueryResponseDto> responseDtoList =
			queryFactory.select(Projections.constructor(ReplyQueryResponseDto.class,
				reply.id,
				reply.contentDetails.userId,
				reply.contentDetails.username,
				reply.contentDetails.content,
				reply.createdAt,
				like.count().as("likeCount")
			))
			.from(reply)
			.leftJoin(like).on(like.target.targetId.eq(reply.id))
			.where(reply.commentId.eq(commentId),
				isNotDeleted(),
				// isNotReported(),
				cursorCondition(cursorId, sortByEnum, cursorLikeCount, cursorCreatedAt))
			.groupBy(reply.id)
			.orderBy(getOrderSpecifier(sortByEnum), reply.id.desc())
			.limit(pageSize + 1)
			.fetch();

		boolean hasNext = responseDtoList.size() > pageSize;
		if (hasNext) {
			responseDtoList.remove(responseDtoList.size() - 1);
		}

		return new SliceImpl<>(responseDtoList, PageRequest.of(0, pageSize), hasNext);
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
			.or(like.count().eq(cursorLikeCount).and(reply.id.gt(cursorId)));
	}

	private BooleanExpression cursorCreatedAtCondition(Long cursorId, LocalDateTime cursorCreatedAt) {
		if (cursorId == null || cursorCreatedAt == null) {
			return null;
		}
		return reply.createdAt.lt(cursorCreatedAt)
			.or(reply.createdAt.eq(cursorCreatedAt).and(reply.id.gt(cursorId)));
	}

	private OrderSpecifier<?> getOrderSpecifier(String sortBy) {
		if ("like".equalsIgnoreCase(sortBy)) {
			return like.count().desc();
		} else {
			return reply.createdAt.desc();
		}
	}
	private BooleanExpression isNotDeleted() {
		return reply.deletedAt.isNull();
	}

	// private BooleanExpression isNotReported() {
	// 	return reply.isReported.eq(false);
	// }
}
