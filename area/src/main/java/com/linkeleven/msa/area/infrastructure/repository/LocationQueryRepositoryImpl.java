package com.linkeleven.msa.area.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.domain.entity.QLocation;
import com.linkeleven.msa.area.domain.vo.Address;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LocationQueryRepositoryImpl implements LocationQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	QLocation qLocation = QLocation.location;

	@Override
	public List<Location> findByLocationInAddress(List<Address> addressList) {
		return jpaQueryFactory.query()
			.select(
				qLocation
			)
			.from(qLocation).where(addressListEquals(addressList))
			.fetch();
	}

	private BooleanExpression addressListEquals(List<Address> addressList) {
		return addressList != null ? qLocation.address.in(addressList) : null;
	}

}
