package com.linkeleven.msa.area.presentation.dto;

import com.linkeleven.msa.area.domain.common.CategoryType;

import lombok.Getter;

@Getter
public class LocationSearchRequestDto {
	private String place;

	private CategoryType category;

}
