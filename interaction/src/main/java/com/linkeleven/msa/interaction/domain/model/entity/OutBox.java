package com.linkeleven.msa.interaction.domain.model.entity;

import java.time.LocalDateTime;

import com.linkeleven.msa.interaction.domain.model.enums.EventStatus;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
@Table(name = "p_outbox")
public class OutBox extends BaseTime{

	@Id
	@Tsid
	private Long id;

	@Column(nullable = false)
	private String eventType;

	@Column(nullable = false)
	private byte[] payload;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EventStatus eventStatus;

	@Column(nullable = false)
	private LocalDateTime processedAt;

	@Builder(access = AccessLevel.PRIVATE)
	private OutBox(String eventType, byte[] payload, EventStatus eventStatus, LocalDateTime processedAt) {
		this.eventType = eventType;
		this.payload = payload;
		this.eventStatus = eventStatus;
		this.processedAt = LocalDateTime.now();
	}

	public static OutBox create(String eventType, byte[] payload, EventStatus eventStatus) {
		return OutBox.builder()
			.eventType(eventType)
			.payload(payload)
			.eventStatus(EventStatus.PENDING)
			.build();
	}

	public void updateStatusProcessed() {
		this.eventStatus = EventStatus.PROCESSED;
		this.processedAt = LocalDateTime.now();
	}

	public void updateStatusFailed() {
		this.eventStatus = EventStatus.FAILED;
		this.processedAt = LocalDateTime.now();
	}
}
