package com.linkeleven.msa.interaction.infrastructure.messaging;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaProducerException;
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
		retryFor = {KafkaProducerException.class},
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
