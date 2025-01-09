package com.linkeleven.msa.area.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.area.application.dto.AreaSearchResponseDto;
import com.linkeleven.msa.area.domain.entity.QArea;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AreaQueryRepositoryImpl implements AreaQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	QArea qArea = QArea.area;

	@Override
	public Page<AreaSearchResponseDto> findAllAreaInKeyword(String keyword, Pageable pageable) {
		List<AreaSearchResponseDto> resultList =  jpaQueryFactory.query().select(
				Projections.constructor(
					AreaSearchResponseDto.class,
					qArea.id,
					ExpressionUtils.as(
						qArea.region.sido
							.concat(" ")
							.concat(qArea.region.sigungu.coalesce(""))
							.concat(" ")
							.concat(qArea.region.eupmyeondong.coalesce(""))
							.concat(" ")
							.concat(qArea.region.ri.coalesce("")),
						"address"
					),
					qArea.region.code
				)
			)
		.from(qArea)
		.where(startsWithCondition(keyword))
		.offset(pageable.getOffset())
		.limit(pageable.getPageSize())
		.fetch();

		long total = Optional.ofNullable(
			jpaQueryFactory
				.select(qArea.count())
				.from(qArea)
				.where(startsWithCondition(keyword))
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(resultList, pageable, total);
	}

	private BooleanExpression startsWithCondition(String keyword) {
		String searchTarget = keyword.trim();
		return qArea.region.sido
			.concat(" ")
			.concat(qArea.region.sigungu.coalesce(""))
			.concat(" ")
			.concat(qArea.region.eupmyeondong.coalesce(""))
			.concat(" ")
			.concat(qArea.region.ri.coalesce(""))
			.startsWith(searchTarget);
	}
}
