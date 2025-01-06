package com.linkeleven.msa.area.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.area.application.dto.PlaceResponseDto;
import com.linkeleven.msa.area.domain.entity.Area;
import com.linkeleven.msa.area.domain.entity.Category;
import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.domain.repository.LocationRepository;
import com.linkeleven.msa.area.domain.vo.Address;
import com.linkeleven.msa.area.domain.vo.Coordinate;
import com.linkeleven.msa.area.domain.vo.PlaceName;
import com.linkeleven.msa.area.libs.exception.CustomException;
import com.linkeleven.msa.area.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationCommandService {
	private final LocationRepository locationRepository;

	@Transactional
	public List<Location> createLocation(
		List<PlaceResponseDto> placeResponseDtoList,
		List<Category> categoryList,
		Area area
	) {
		log.info("areaId: {}, sido {}", area.getId(), area.getRegion().getSido());

		// 기존에 존재하는 Location 조회
		List<Location> existedLocationList = findExistedLocation(placeResponseDtoList);

		// 결과를 담을 리스트
		List<Location> resultList = new ArrayList<>();
		List<Location> saveList = new ArrayList<>();

		for (PlaceResponseDto placeResponseDto : placeResponseDtoList) {

			Location location = existedLocationList.stream()
				.filter(loc -> loc.getCoordinate().getMapX() == Integer.parseInt(placeResponseDto.getMapX()))
				.filter(loc -> loc.getCoordinate().getMapY() == Integer.parseInt(placeResponseDto.getMapY()))
				.findFirst()
				.orElse(null);

			if (location != null) {
				resultList.add(location);
				continue;
			}

			Coordinate coordinate = Coordinate.of(
				Integer.parseInt(placeResponseDto.getMapX()),
				Integer.parseInt(placeResponseDto.getMapY())
			);
			PlaceName placeName = PlaceName.from(placeResponseDto.getTitle());
			Address address = Address.from(placeResponseDto.getAddress());
			location = Location.createLocation(coordinate, placeName, address);

			Category category = categoryList.stream()
				.filter(cat -> cat.getCategoryName().equals(placeResponseDto.getCategory()))
				.findFirst()
				.orElseThrow(() -> new CustomException(ErrorCode.ILLEGAL_MATCH_CATEGORY));

			location.updateCategory(category);
			location.updateArea(area);

			saveList.add(location);
		}

		List<Location> savedLocations = locationRepository.saveAll(saveList);

		resultList.addAll(savedLocations);

		return resultList;
	}



	private List<Location> findExistedLocation(List<PlaceResponseDto> placeResponseDtoList) {
		List<Address> addressList =
			placeResponseDtoList.stream()
				.map(dto -> Address.from(dto.getAddress()))
				.toList();
		return locationRepository.findByLocationInAddress(addressList);
	}


}
