package com.linkeleven.msa.area.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.area.application.dto.LocationSearchDetailResponseDto;
import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.domain.common.CategoryType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationQueryService {
	// elastic service

	private final SearchLocationService searchLocationService;

	public List<LocationSearchResponseDto> searchLocationByKeyword(String keyword, CategoryType type, Long areaId){

		return searchLocationService.searchKeyword(keyword, type, areaId);
	}

	public LocationSearchDetailResponseDto searchDetailLocation(Long locationId){

		return searchLocationService.searchLocation(locationId);
	}


}
