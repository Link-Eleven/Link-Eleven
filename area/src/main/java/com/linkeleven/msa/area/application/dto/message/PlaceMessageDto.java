package com.linkeleven.msa.area.application.dto.message;

import com.linkeleven.msa.area.domain.entity.Area;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceMessageDto {
	private Long areaId;
	private String keyword;
	private String regionKeyword;

	public static PlaceMessageDto of(Area area, String keyword, String regionKeyword){
		return PlaceMessageDto.builder()
			.areaId(area.getId())
			.keyword(keyword)
			.regionKeyword(regionKeyword)
			.build();
	}
}
