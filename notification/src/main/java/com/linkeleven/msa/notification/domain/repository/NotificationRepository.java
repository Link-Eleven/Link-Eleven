package com.linkeleven.msa.notification.domain.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.notification.domain.model.entity.Notification;

import jakarta.transaction.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	@Modifying
	@Transactional
	@Query("DELETE FROM Notification n WHERE n.createdAt < :deleteBeforeTime")
	void deleteByCreatedAtBefore(@Param("deleteBeforeTime")LocalDateTime deleteTargetNotification);
}
