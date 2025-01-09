package com.linkeleven.msa.feed.infrastructure.repository;

import static com.linkeleven.msa.feed.domain.model.QFeed.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.domain.enums.Region;
import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.domain.repository.FeedRepositoryCustom;
import com.linkeleven.msa.feed.presentation.request.FeedSearchRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<FeedSearchResponseDto> searchFeeds(FeedSearchRequestDto searchRequestDto, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(feed.deletedAt.isNull());

		if (searchRequestDto.getTitle() != null && !searchRequestDto.getTitle().isEmpty()) {
			builder.and(titleContains(searchRequestDto.getTitle()));
		}

		if (searchRequestDto.getContent() != null && !searchRequestDto.getContent().isEmpty()) {
			builder.and(contentContains(searchRequestDto.getContent()));
		}

		if (searchRequestDto.getRegionEnum() != null) {
			builder.and(regionEq(searchRequestDto.getRegionEnum()));
		}

		if (searchRequestDto.getCategory() != null) {
			builder.and(categoryEq(searchRequestDto.getCategory()));
		}

		JPQLQuery<FeedSearchResponseDto> query = queryFactory.select(
				Projections.constructor(FeedSearchResponseDto.class,
					feed.feedId,
					feed.title,
					feed.content,
					feed.category,
					feed.region
				)
			)
			.from(feed)
			.where(builder)
			.orderBy(feed.createdAt.desc());

		List<FeedSearchResponseDto> dtoResults = query
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = dtoResults.size() > pageable.getPageSize();
		if (hasNext) {
			dtoResults.remove(dtoResults.size() - 1);
		}

		return new SliceImpl<>(dtoResults, pageable, hasNext);
	}

	private BooleanExpression titleContains(String title) {
		return title != null ? feed.title.containsIgnoreCase(title) : null;
	}

	private BooleanExpression contentContains(String content) {
		return content != null ? feed.content.containsIgnoreCase(content) : null;
	}

	private BooleanExpression regionEq(Region region) {
		return region != null ? feed.region.eq(region) : null;
	}

	private BooleanExpression categoryEq(Category category) {
		return category != null ? feed.category.eq(category) : null;
	}

}
