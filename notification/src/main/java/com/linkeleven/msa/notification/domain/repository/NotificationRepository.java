package com.linkeleven.msa.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.notification.domain.model.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
