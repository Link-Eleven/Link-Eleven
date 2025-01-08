package com.linkeleven.msa.area.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.area.application.dto.AreaSearchResponseDto;
import com.linkeleven.msa.area.domain.entity.Area;
import com.linkeleven.msa.area.domain.repository.AreaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AreaRepositoryImpl implements AreaRepository {

	private final AreaQueryRepository areaQueryRepository;
	private final AreaJpaRepository areaJpaRepository;

	@Override
	public Optional<Area> findById(Long areaId) {
		return areaJpaRepository.findById(areaId);
	}

	@Override
	public Page<AreaSearchResponseDto> findAllAreaInKeyword(String keyword, Pageable pageable) {
		return areaQueryRepository.findAllAreaInKeyword(keyword, pageable);
	}
}
