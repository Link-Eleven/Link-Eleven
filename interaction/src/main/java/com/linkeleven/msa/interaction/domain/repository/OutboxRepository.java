package com.linkeleven.msa.interaction.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.interaction.domain.model.entity.OutBox;

public interface OutboxRepository extends JpaRepository<OutBox, Long> {
}
