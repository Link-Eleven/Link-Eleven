package com.linkeleven.msa.area.infrastructure.repository;

import java.util.List;

import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.domain.vo.Address;

public interface LocationQueryRepository {

	List<Location> findByLocationInAddress(List<Address> addressList);

}
