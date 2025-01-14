package com.linkeleven.msa.area.application.service.message;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.area.application.dto.PlaceResponseDto;
import com.linkeleven.msa.area.application.dto.message.PlaceMessageDto;
import com.linkeleven.msa.area.application.service.CategoryCommandService;
import com.linkeleven.msa.area.application.service.LocationCommandService;
import com.linkeleven.msa.area.application.service.SearchLocationService;
import com.linkeleven.msa.area.application.service.SearchNaverService;
import com.linkeleven.msa.area.domain.entity.Area;
import com.linkeleven.msa.area.domain.entity.Category;
import com.linkeleven.msa.area.domain.entity.Location;
import com.linkeleven.msa.area.domain.repository.AreaRepository;
import com.linkeleven.msa.area.libs.exception.CustomException;
import com.linkeleven.msa.area.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceConsumerService {
	private final SearchNaverService searchNaverService;
	private final AreaRepository areaRepository;
	private final LocationCommandService locationCommandService;
	private final CategoryCommandService categoryCommandService;
	private final SearchLocationService searchLocationService;

	@KafkaListener(groupId = "area-group", topics = "place-create-topic")
	@RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000, multiplier = 1.1))
	@Transactional
	public void consumePlaceCreateMessage(PlaceMessageDto placeMessage) {

		log.info("keyword: {} message consumed", placeMessage.getKeyword());
		List<PlaceResponseDto> responseDtoList =
			searchNaverService.searchPlace(placeMessage.getRegionKeyword(), 5, 1);

		if (responseDtoList.isEmpty()) {
			// 네이버 Place API 조회시 없을경우 큐 종료 처리
			return;
		}

		List<Category> categoryList = categoryCommandService.createCategoryList(responseDtoList);

		Area area = areaRepository.findById(placeMessage.getAreaId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AREA));

		List<Location> locationList;
		locationList = locationCommandService.createLocation(
			responseDtoList,
			categoryList,
			area
		);

		searchLocationService.create(locationList, placeMessage.getKeyword());

	}

}
