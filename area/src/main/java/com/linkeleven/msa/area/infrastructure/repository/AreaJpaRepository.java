package com.linkeleven.msa.area.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.area.domain.entity.Area;
import com.linkeleven.msa.area.domain.repository.AreaRepository;

public interface AreaJpaRepository extends JpaRepository<Area, Long>, AreaRepository {



}
