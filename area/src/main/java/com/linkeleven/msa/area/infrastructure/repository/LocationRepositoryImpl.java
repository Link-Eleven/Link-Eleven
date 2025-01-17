package com.linkeleven.msa.area.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.domain.repository.LocationRepository;
import com.linkeleven.msa.area.domain.vo.Address;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
	private final LocationJpaRepository locationJpaRepository;
	private final LocationQueryRepository locationQueryRepository;

	@Override
	public List<Location> findByLocationInAddress(
		List<Address> addressList) {
		return locationQueryRepository.findByLocationInAddress(addressList);
	}

	@Override
	public List<Location> saveAll(List<Location> locationList) {
		return locationJpaRepository.saveAll(locationList);
	}

}
