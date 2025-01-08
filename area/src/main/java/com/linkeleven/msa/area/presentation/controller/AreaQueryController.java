package com.linkeleven.msa.area.presentation.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.area.application.dto.AreaSearchResponseDto;
import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.application.service.AreaQueryService;
import com.linkeleven.msa.area.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.area.presentation.dto.LocationSearchRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/areas")
public class AreaQueryController {

	private final AreaQueryService areaQueryService;

	@PostMapping("/{areaId}/locations")
	public ResponseEntity<SuccessResponseDto<List<LocationSearchResponseDto>>> searchLocationByRegion(
		@PathVariable Long areaId,
		@RequestBody LocationSearchRequestDto locationSearchRequestDto
	) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(
				SuccessResponseDto.success(
					"장소 정보 조회 및 생성 성공",
					areaQueryService.searchLocationByRegion(areaId, locationSearchRequestDto))
			);
	}

	@GetMapping
	public ResponseEntity<SuccessResponseDto<Page<AreaSearchResponseDto>>> searchAreaByKeyword(
		@RequestParam String keyword,
		Pageable pageable
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(
				SuccessResponseDto.success(
					"주소 정보 조회 성공",
					areaQueryService.searchAreaByKeyword(keyword, pageable)
				)
			);
	}

}
