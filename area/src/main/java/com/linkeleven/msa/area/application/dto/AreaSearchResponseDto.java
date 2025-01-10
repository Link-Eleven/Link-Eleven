package com.linkeleven.msa.area.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AreaSearchResponseDto {
	private Long areaId;
	private String address;
	private String addressCode;
}
