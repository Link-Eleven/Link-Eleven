package com.linkeleven.msa.area.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponseDto {

	private String title;
	private String category;
	private String address;
	private String mapX;
	private String mapY;

	public static PlaceResponseDto of(
		String title,
		String category,
		String address,
		String mapX,
		String mapY
	) {
		return PlaceResponseDto.builder()
			.title(title)
			.category(category)
			.address(address)
			.mapX(mapX)
			.mapY(mapY)
			.build();
	}
}
