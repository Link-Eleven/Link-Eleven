package com.linkeleven.msa.area.application.dto;

import com.linkeleven.msa.area.infrastructure.elasticsearch.SearchLocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationSearchResponseDto {
	private String searchLocationId;
	private Long locationId;
	private Long areaId;
	private int mapX;
	private int mapY;
	private String placeName;
	private String address;
	private String category;

	public static LocationSearchResponseDto from(SearchLocation searchLocation){
		return LocationSearchResponseDto.builder()
			.searchLocationId(searchLocation.getId())
			.locationId(searchLocation.getLocationId())
			.areaId(searchLocation.getAreaId())
			.mapX(searchLocation.getMapX())
			.mapY(searchLocation.getMapY())
			.placeName(searchLocation.getPlaceName())
			.address(searchLocation.getAddress())
			.category(searchLocation.getCategory())
			.build();
	}

}
