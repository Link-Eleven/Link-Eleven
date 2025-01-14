package com.linkeleven.msa.coupon.infrastructure.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.domain.model.QCoupon;
import com.linkeleven.msa.coupon.domain.model.QCouponPolicy;
import com.linkeleven.msa.coupon.domain.model.QIssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final QCoupon coupon = QCoupon.coupon;
	private final QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
	private final QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public Page<CouponSearchResponseDto> findCouponsByFilter(Long userId, CouponPolicyStatus status, Long feedId,
		String validFrom, String validTo, Pageable pageable) {

		BooleanExpression createdByCondition = userId != null ? coupon.createdBy.eq(userId) : null;

		List<CouponSearchResponseDto> content = queryFactory
			.select(Projections.constructor(CouponSearchResponseDto.class,
				coupon.couponId,
				coupon.feedId,
				couponPolicy.status,
				coupon.validFrom,
				coupon.validTo,
				queryFactory.select(issuedCoupon.count())
					.from(issuedCoupon)
					.where(issuedCoupon.couponId.eq(coupon.couponId))
			))
			.from(coupon)
			.join(coupon.policies, couponPolicy)
			.where(
				createdByCondition,
				policyStatusEq(status),
				feedIdEq(feedId),
				validFromGoe(validFrom),
				validToLoe(validTo))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.groupBy(
				coupon.couponId,
				coupon.feedId,
				couponPolicy.status,
				coupon.validFrom,
				coupon.validTo)
			.fetch();

		Long total = queryFactory.select(coupon.count())
			.from(coupon)
			.join(coupon.policies, couponPolicy)
			.where(
				createdByCondition,
				policyStatusEq(status),
				feedIdEq(feedId),
				validFromGoe(validFrom),
				validToLoe(validTo))
			.fetchOne();

		total = total != null ? total : 0L;

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression policyStatusEq(CouponPolicyStatus status) {
		return status != null ? couponPolicy.status.eq(status) : null;
	}

	private BooleanExpression feedIdEq(Long feedId) {
		return feedId != null ? coupon.feedId.eq(feedId) : null;
	}

	private BooleanExpression validFromGoe(String validFrom) {
		if (StringUtils.isEmpty(validFrom)) {
			return null;
		}
		return coupon.validFrom.goe(LocalDateTime.parse(validFrom + " 00:00:00", formatter));
	}

	private BooleanExpression validToLoe(String validTo) {
		if (StringUtils.isEmpty(validTo)) {
			return null;
		}
		return coupon.validTo.loe(LocalDateTime.parse(validTo + " 23:59:59", formatter));
	}
}