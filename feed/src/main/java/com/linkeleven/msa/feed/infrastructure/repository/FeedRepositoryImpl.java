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
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.repository.FeedRepositoryCustom;
import com.linkeleven.msa.feed.presentation.request.FeedSearchRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<FeedSearchResponseDto> searchFeeds(FeedSearchRequestDto searchRequestDto, Pageable pageable) {

		List<Feed> results = queryFactory.selectFrom(feed)
			.where(
				feed.deletedAt.isNull(),
				titleContains(searchRequestDto.getTitle()),
				contentContains(searchRequestDto.getContent()),
				regionEq(searchRequestDto.getRegion()),
				categoryEq(searchRequestDto.getCategory())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.selectFrom(feed)
			.where(
				feed.deletedAt.isNull(),
				titleContains(searchRequestDto.getTitle()),
				contentContains(searchRequestDto.getContent()),
				// regionEq(searchRequestDto.getRegion()),
				categoryEq(searchRequestDto.getCategory())
			)
			.fetch()
			.size();

		List<FeedSearchResponseDto> dtoResults = results.stream()
			.map(FeedSearchResponseDto::from)
			.toList();

		boolean hasNext = dtoResults.size() > pageable.getPageSize();

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
