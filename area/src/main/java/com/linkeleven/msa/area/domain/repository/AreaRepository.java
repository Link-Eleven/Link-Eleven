package com.linkeleven.msa.area.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linkeleven.msa.area.application.dto.AreaSearchResponseDto;
import com.linkeleven.msa.area.domain.entity.Area;

public interface AreaRepository {

	Optional<Area> findById(Long areaId);

	Page<AreaSearchResponseDto> findAllAreaInKeyword(String keyword, Pageable pageable);
}
