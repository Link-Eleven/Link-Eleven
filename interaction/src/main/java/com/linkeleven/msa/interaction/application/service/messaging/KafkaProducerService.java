package com.linkeleven.msa.interaction.application.service.messaging;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.common.errors.NetworkException;
import org.apache.kafka.common.errors.NotEnoughReplicasException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.domain.model.entity.OutBox;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Retryable(
		retryFor = {
			NetworkException.class,
			TimeoutException.class,
			NotEnoughReplicasException.class,
			DataAccessResourceFailureException.class,
			TransientDataAccessException.class
		},
		noRetryFor = {
			IllegalAccessException.class,
			NullPointerException.class,
			ClassCastException.class
		},
		maxAttempts = 5,
		backoff = @Backoff(
			delay = 1000
		)
	)
	public  CompletableFuture<SendResult<String, Object>> sendEvent(OutBox outBox) {
		String topic = outBox.getEventType().toLowerCase();
		String key = outBox.getId().toString();
		return kafkaTemplate.send(topic, key, outBox.getPayload());
	}
}
