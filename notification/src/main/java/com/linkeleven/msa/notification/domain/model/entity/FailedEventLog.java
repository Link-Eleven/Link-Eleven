package com.linkeleven.msa.notification.domain.model.entity;

import java.time.LocalDateTime;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "p_failedEventLog")
public class FailedEventLog {

	@Id
	@Tsid
	private Long id;

	@Column(nullable = false)
	private String topicName;

	@Column(nullable = false)
	private String failedMessage;

	@Column(nullable = false)
	private LocalDateTime failedAt;

	@Builder(access = AccessLevel.PRIVATE)
	public FailedEventLog(String topicName, String failedMessage) {
		this.topicName = topicName;
		this.failedMessage = failedMessage;
		this.failedAt = LocalDateTime.now();
	}

	public static FailedEventLog create(String topicName, String failedMessage) {
		return FailedEventLog.builder()
			.topicName(topicName)
			.failedMessage(failedMessage)
			.build();
	}
}
