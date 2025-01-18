package com.linkeleven.msa.notification.domain.model.entity;

import java.time.LocalDateTime;

import com.linkeleven.msa.notification.domain.model.enums.NotificationType;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_notification")
public class Notification {

	@Id
	@Tsid
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;

	@Column(nullable = false)
	private Long targetId;

	@Column(nullable = false)
	private Long targetAuthorId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Builder(access = AccessLevel.PRIVATE)
	private Notification(NotificationType type, Long targetId, Long targetAuthorId, Long userId, String message) {
		this.type = type;
		this.targetId = targetId;
		this.targetAuthorId = targetAuthorId;
		this.userId = userId;
		this.message = message;
		this.createdAt = LocalDateTime.now();
	}

	public static Notification create(NotificationType type, Long targetId, Long targetAuthorId, Long userId, String username, String contentType) {
		String message = type.generateMessage(username, contentType);
		return Notification.builder()
			.type(type)
			.targetId(targetId)
			.targetAuthorId(targetAuthorId)
			.userId(userId)
			.message(message)
			.build();
	}

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}
