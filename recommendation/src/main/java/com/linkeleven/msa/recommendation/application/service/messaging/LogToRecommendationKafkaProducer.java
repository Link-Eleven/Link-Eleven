package com.linkeleven.msa.recommendation.application.service.messaging;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.recommendation.application.dto.message.RecommendationMessage;
import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogToRecommendationKafkaProducer {

	private final KafkaTemplate<String, RecommendationMessage> kafkaTemplate;
	private static final String TOPIC = "log-recommendation-topic";

	@Retryable(
		maxAttempts = 5,
		backoff = @Backoff(delay = 2000)
	)
	public void sendToRecommendationService(Recommendation generatedKeywords) {
		RecommendationMessage message = buildRecommendationMessage(generatedKeywords);
		sendKafkaMessage(message, generatedKeywords);
	}

	private RecommendationMessage buildRecommendationMessage(Recommendation generatedKeywords) {
		return RecommendationMessage.builder()
			.userId(generatedKeywords.getUserId())
			.recommendationKeywords(generatedKeywords.getKeywords())
			.timestamp(LocalDateTime.now())
			.build();
	}

	private void sendKafkaMessage(RecommendationMessage message, Recommendation generatedKeywords) {
		kafkaTemplate.send(TOPIC, message)
			.whenComplete((result, ex) -> handleKafkaSendResult(ex, generatedKeywords));
	}

	private void handleKafkaSendResult(Throwable ex, Recommendation generatedKeywords) {
		if (ex == null) {
			log.info("사용자에 대한 키워드를 성공적으로 보냈습니다.: {}", generatedKeywords.getUserId());
		} else {
			log.error("키워드 전송에 실패했습니다: userId = {}, error = {}", generatedKeywords.getUserId(), ex.getMessage(), ex);
			throw new CustomException(ErrorCode.KAFKA_SEND_FAILURE);
		}
	}
}