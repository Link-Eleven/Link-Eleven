package com.linkeleven.msa.area.domain.repository;

import java.util.Optional;

import com.linkeleven.msa.area.domain.entity.Area;

public interface AreaRepository {

	Optional<Area> findById(Long areaId);
}
