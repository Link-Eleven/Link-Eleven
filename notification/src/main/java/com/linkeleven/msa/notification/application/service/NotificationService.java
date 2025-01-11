package com.linkeleven.msa.notification.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.notification.domain.model.entity.Notification;
import com.linkeleven.msa.notification.domain.model.enums.NotificationType;
import com.linkeleven.msa.notification.domain.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public void saveNotificationEvent(
		NotificationType type,
		Long targetId, Long targetAuthorId,
		Long userId, String username, String contentType)
	{
		Notification notification = Notification.create(
			type,
			targetId,
			targetAuthorId,
			userId,
			username,
			contentType
		);
		notificationRepository.save(notification);
	}
}
