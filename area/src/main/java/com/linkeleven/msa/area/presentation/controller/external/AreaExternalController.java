package com.linkeleven.msa.area.presentation.controller.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.area.application.dto.external.AdministrativeRegionDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/area")
public class AreaExternalController {


	// 내가 해당하는 지역의 행정구역
	@GetMapping("/location")
	public AdministrativeRegionDto findAdministrativeRegion(
		@RequestParam double latitude,
		@RequestParam double longitude
	){

		return AdministrativeRegionDto.builder()
			.regionName("서울특별시")
			.build();
	}

	// 해당 주소 입력의 지역 행정구역
	@GetMapping("/location/{address}")
	public AdministrativeRegionDto findAddressRegion(
		@PathVariable String address
	){
		return AdministrativeRegionDto.builder()
			.regionName("서울특별시")
			.build();
	}

	// 해당 키워드 입력의 지역 행정구역
	@GetMapping("/location/keyword")
	public AdministrativeRegionDto findKeywordByRegion(
		@RequestParam String keyword
	){
		return AdministrativeRegionDto.builder()
			.regionName("서울특별시")
			.build();
	}


}
