package com.linkeleven.msa.area.application.service;

import java.util.List;

import com.linkeleven.msa.area.application.dto.PlaceResponseDto;

public interface SearchNaverService {

	List<PlaceResponseDto> searchPlace(String query, int display, int start);
}
