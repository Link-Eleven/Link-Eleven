package com.linkeleven.msa.area.application.service;

import java.util.List;

import com.linkeleven.msa.area.application.dto.LocationSearchDetailResponseDto;
import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.domain.common.CategoryType;
import com.linkeleven.msa.area.domain.entity.Location;

public interface SearchLocationService {

	List<LocationSearchResponseDto> searchKeyword(String keyword, CategoryType categoryType, Long areaId);

	void create(List<Location> locationList, String keyword);

	LocationSearchDetailResponseDto searchLocation(Long locationId);
}
