package com.linkeleven.msa.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.notification.domain.model.entity.FailedEventLog;

public interface FailedEventLogRepository extends JpaRepository<FailedEventLog, Long> {
}
