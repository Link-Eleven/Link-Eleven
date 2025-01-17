package com.linkeleven.msa.interaction.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.interaction.domain.model.entity.OutBox;
import com.linkeleven.msa.interaction.domain.model.enums.EventStatus;

public interface OutboxRepository extends JpaRepository<OutBox, Long> {
	List<OutBox> findByEventStatus(EventStatus eventStatus);
}
