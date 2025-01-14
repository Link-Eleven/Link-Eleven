package com.linkeleven.msa.recommendation.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.recommendation.application.dto.RecommendationCommand;
import com.linkeleven.msa.recommendation.application.service.RecommendationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationConsumer {

	private final RecommendationService recommendationService;

	@KafkaListener(topics = "recommendationKeywords", groupId = "${spring.kafka.consumer.group-id}")
	@Retryable(
		maxAttempts = 3,  // 최대 3번 재시도
		backoff = @Backoff(delay = 2000)  // 2초 간격으로 재시도
	)
	public void consume(RecommendationMessage message) {
		if (message == null || message.getUserId() == null) {
			log.error("Invalid message received: {}", message);
			return;
		}

		try {
			RecommendationCommand command = RecommendationCommand.from(message);
			recommendationService.processRecommendation(command);
		} catch (MessageConversionException e) {
			log.error("메시지 변환 중 오류 발생 - message: {}", message, e);
		} catch (Exception e) {
			log.error("예기치 않은 오류 발생 - message: {}", message, e);
			throw e;
		}
	}
}