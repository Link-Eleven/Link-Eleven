package com.linkeleven.msa.recommendation.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.converter.MessageConversionException;
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

	@KafkaListener(topics = "recommendationKeywords", groupId = "${kafka.consumer.recommendation-service-group-id}")
	public void consume(RecommendationMessage message) {
		if (isInvalidMessage(message)) {
			handleInvalidMessage(message);
			return;
		}

		try {
			RecommendationCommand command = convertToCommand(message);
			processRecommendation(command);
		} catch (MessageConversionException e) {
			handleMessageConversionException(message, e);
		} catch (Exception e) {
			handleUnexpectedException(message, e);
		}
	}

	private boolean isInvalidMessage(RecommendationMessage message) {
		return message == null || message.getUserId() == null;
	}

	private void handleInvalidMessage(RecommendationMessage message) {
		log.error("Invalid message received: {}", message);
	}

	private RecommendationCommand convertToCommand(RecommendationMessage message) {
		try {
			return RecommendationCommand.from(message);
		} catch (Exception e) {
			log.error("메시지 변환 중 오류 발생 - message: {}", message, e);
			throw new MessageConversionException("Error converting message", e);
		}
	}

	private void processRecommendation(RecommendationCommand command) {
		recommendationService.processRecommendation(command);
	}

	private void handleMessageConversionException(RecommendationMessage message, MessageConversionException e) {
		log.error("메시지 변환 중 오류 발생 - userId: {}, message: {}", message.getUserId(), message, e);
		throw e;
	}

	private void handleUnexpectedException(RecommendationMessage message, Exception e) {
		log.error("예기치 않은 오류 발생 - userId: {}, message: {}", message.getUserId(), message, e);
		throw new RuntimeException(e);
	}
}