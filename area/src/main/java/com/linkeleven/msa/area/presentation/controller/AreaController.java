package com.linkeleven.msa.area.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.application.service.AreaService;
import com.linkeleven.msa.area.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.area.presentation.dto.LocationSearchRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/areas")
public class AreaController {

	private final AreaService areaService;

	@PostMapping("/{areaId}/locations")
	public ResponseEntity<SuccessResponseDto<List<LocationSearchResponseDto>>> searchLocationByRegion(
		// @RequestHeader("X-User-Id") Long userId,
		@PathVariable Long areaId,
		@RequestBody LocationSearchRequestDto locationSearchRequestDto
	){

		return ResponseEntity.status(HttpStatus.OK)
			.body(
				SuccessResponseDto.success(
					"장소 정보 조회 및 생성 성공",
					areaService.searchLocationByRegion(areaId, locationSearchRequestDto))
			);
	}

	// 조회 X


}
