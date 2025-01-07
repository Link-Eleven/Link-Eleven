package com.linkeleven.msa.area.domain.repository;

import java.util.List;

import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.domain.vo.Address;

public interface LocationRepository {

	List<Location> findByLocationInAddress(List<Address> addressList);

	List<Location> saveAll(List<Location> locationList);

}
