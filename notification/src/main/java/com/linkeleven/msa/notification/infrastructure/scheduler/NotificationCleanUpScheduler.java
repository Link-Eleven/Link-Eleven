package com.linkeleven.msa.notification.infrastructure.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.notification.domain.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationCleanUpScheduler {

	private final NotificationRepository notificationRepository;

	@Scheduled(cron = "0 0 2 * * ?")
	public void deleteOldNotification() {
		LocalDateTime deleteBeforeTime = LocalDateTime.now().minusMonths(3);
		notificationRepository.deleteByCreatedAtBefore(deleteBeforeTime);
	}
}
