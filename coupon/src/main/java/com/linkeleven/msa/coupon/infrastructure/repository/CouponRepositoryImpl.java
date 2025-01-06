package com.linkeleven.msa.coupon.infrastructure.repository;

import java.time.LocalDateTime;
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
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<CouponSearchResponseDto> findCouponsByFilter(CouponPolicyStatus status, Long feedId, String validFrom,
		String validTo, Pageable pageable) {
		QCoupon coupon = QCoupon.coupon;
		QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
		QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

		List<CouponSearchResponseDto> content = queryFactory
			.select(Projections.constructor(CouponSearchResponseDto.class,
				coupon.couponId,
				coupon.feedId,
				couponPolicy.status,
				coupon.validFrom,
				coupon.validTo,
				queryFactory.select(issuedCoupon.count())
					.from(issuedCoupon)
					.where(issuedCoupon.couponId.eq(coupon.couponId)
						.and(issuedCoupon.status.ne(IssuedCouponStatus.DELETED)))
				,
				queryFactory.select(issuedCoupon.count())
					.from(issuedCoupon)
					.where(issuedCoupon.couponId.eq(coupon.couponId)
						.and(issuedCoupon.status.eq(IssuedCouponStatus.USED)))
			))
			.from(coupon)
			.join(coupon.policies, couponPolicy)
			.where(
				policyStatusEq(status),
				feedIdEq(feedId),
				validFromGoe(validFrom),
				validToLoe(validTo),
				couponPolicy.status.ne(CouponPolicyStatus.DELETED)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.groupBy(
				coupon.couponId,
				coupon.feedId,
				couponPolicy.status,
				coupon.validFrom,
				coupon.validTo
			)
			.fetch();

		long total = queryFactory.selectFrom(coupon)
			.join(coupon.policies, couponPolicy)
			.where(
				policyStatusEq(status),
				feedIdEq(feedId),
				validFromGoe(validFrom),
				validToLoe(validTo),
				couponPolicy.status.ne(CouponPolicyStatus.DELETED)
			)
			.fetchCount();

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression policyStatusEq(CouponPolicyStatus status) {
		return status != null ? QCouponPolicy.couponPolicy.status.eq(status) : null;
	}

	private BooleanExpression feedIdEq(Long feedId) {
		return feedId != null ? QCoupon.coupon.feedId.eq(feedId) : null;
	}

	private BooleanExpression validFromGoe(String validFrom) {
		if (StringUtils.isEmpty(validFrom)) {
			return null;
		}
		return QCoupon.coupon.validFrom.goe(LocalDateTime.parse(validFrom + "T00:00:00"));
	}

	private BooleanExpression validToLoe(String validTo) {
		if (StringUtils.isEmpty(validTo)) {
			return null;
		}
		return QCoupon.coupon.validTo.loe(LocalDateTime.parse(validTo + "T23:59:59"));
	}
}
