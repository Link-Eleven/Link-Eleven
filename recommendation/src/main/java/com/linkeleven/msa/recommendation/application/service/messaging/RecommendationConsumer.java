package com.linkeleven.msa.recommendation.application.service.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.recommendation.application.dto.KeywordRecommendationDto;
import com.linkeleven.msa.recommendation.application.dto.message.RecommendationMessage;
import com.linkeleven.msa.recommendation.application.service.RecommendationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationConsumer {

	private final RecommendationService recommendationService;

	@KafkaListener(topics = "log-recommendation-topic", groupId = "${spring.kafka.consumer.group-id}")
	@Retryable(
		maxAttempts = 5,
		backoff = @Backoff(delay = 2000)
	)
	public void consumeRecommendationMessage(RecommendationMessage message) {
		if (isInvalidMessage(message)) {
			log.error("잘못된 메시지를 받았습니다: {}", message);
			return;
		}

		try {
			KeywordRecommendationDto keywords = KeywordRecommendationDto.from(message);
			recommendationService.processRecommendation(keywords);
		} catch (MessageConversionException e) {
			log.error("메시지 변환 중 오류 발생 - message: {}", message, e);
		} catch (Exception e) {
			log.error("예기치 않은 오류 발생 - message: {}", message, e);
			throw e;
		}
	}

	private boolean isInvalidMessage(RecommendationMessage message) {
		return message == null || message.getUserId() == null;
	}
}