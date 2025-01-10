package com.linkeleven.msa.area.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linkeleven.msa.area.application.dto.AreaSearchResponseDto;

public interface AreaQueryRepository {
	Page<AreaSearchResponseDto> findAllAreaInKeyword(String keyword, Pageable pageable);
}
