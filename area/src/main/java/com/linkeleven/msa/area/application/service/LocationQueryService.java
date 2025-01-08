package com.linkeleven.msa.area.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.area.application.dto.LocationSearchDetailResponseDto;
import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.domain.common.CategoryType;
import com.linkeleven.msa.area.infrastructure.elasticsearch.SearchLocationServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationQueryService {
	// elastic service

	private final SearchLocationServiceImpl searchLocationService;

	public List<LocationSearchResponseDto> searchLocationByKeyword(String keyword, CategoryType type, Long areaId){

		return searchLocationService.searchKeyword(keyword, type, areaId);
	}

	public LocationSearchDetailResponseDto searchDetailLocation(Long locationId){

		return searchLocationService.searchLocation(locationId);
	}


}
