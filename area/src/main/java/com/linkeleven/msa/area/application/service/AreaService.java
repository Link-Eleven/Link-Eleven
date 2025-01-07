package com.linkeleven.msa.area.application.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.area.application.dto.LocationSearchResponseDto;
import com.linkeleven.msa.area.application.dto.message.PlaceMessageDto;
import com.linkeleven.msa.area.application.service.message.PlaceProduceService;
import com.linkeleven.msa.area.domain.entity.Area;
import com.linkeleven.msa.area.domain.repository.AreaRepository;
import com.linkeleven.msa.area.domain.vo.Region;
import com.linkeleven.msa.area.libs.exception.CustomException;
import com.linkeleven.msa.area.libs.exception.ErrorCode;
import com.linkeleven.msa.area.presentation.dto.LocationSearchRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaService {
	private final AreaRepository areaRepository;
	private final LocationQueryService locationQueryService;
	private final PlaceProduceService placeProduceService;

	public List<LocationSearchResponseDto> searchLocationByRegion(
		Long areaId,
		LocationSearchRequestDto locationSearchRequestDto
	){

		// TODO: 2025-01-03 유저 로그인 인증 추가 예정
		Area area = areaRepository.findById(areaId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_AREA)
		);

		// ES 조회
		List<LocationSearchResponseDto> responseDtoList =
			locationQueryService.searchLocationByKeyword(
				locationSearchRequestDto.getPlace(),
				locationSearchRequestDto.getCategory(),
				area.getId()
			);
		if(responseDtoList.isEmpty()){
			// kafka 요청 Category, Location, ES 한번에 처리
			String keyword = locationSearchRequestDto.getPlace();
			String regionKeyword = generateRegionKeyword(area, locationSearchRequestDto.getPlace());
			PlaceMessageDto messageDto = PlaceMessageDto.of(area, keyword ,regionKeyword);
			placeProduceService.sendPlaceCreateMessage(messageDto);
		}

		return responseDtoList;
	}

	private String generateRegionKeyword(Area area, String keyword){
		Region region = area.getRegion();

		return Stream.of(
				region.getSido(),
				region.getSigungu(),
				region.getEupmyeondong(),
				region.getRi(),
				keyword
			)
			.filter(Objects::nonNull)
			.filter(s -> !s.isEmpty())
			.collect(Collectors.joining(" "));
	}
}
