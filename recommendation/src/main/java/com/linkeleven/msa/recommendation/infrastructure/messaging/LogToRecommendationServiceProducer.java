package com.linkeleven.msa.recommendation.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogToRecommendationServiceProducer {
	private final KafkaTemplate<String, RecommendationMessage> kafkaTemplate;
	private static final String TOPIC = "recommendationKeywords";

	public void sendToRecommendationService(Recommendation analysis) {
		RecommendationMessage message = buildRecommendationMessage(analysis);
		sendKafkaMessage(message, analysis);
	}

	private RecommendationMessage buildRecommendationMessage(Recommendation analysis) {
		return RecommendationMessage.builder()
			.userId(analysis.getUserId())
			.recommendationKeywords(analysis.getKeywords())
			.build();
	}

	private void sendKafkaMessage(RecommendationMessage message, Recommendation analysis) {
		kafkaTemplate.send(TOPIC, message)
			.whenComplete((result, ex) -> handleKafkaSendResult(ex, analysis));
	}

	private void handleKafkaSendResult(Throwable ex, Recommendation analysis) {
		if (ex == null) {
			log.info("사용자에 대한 키워드를 성공적으로 보냈습니다.: {}", analysis.getUserId());
		} else {
			log.error("키워드 전송에 실패했습니다: userId = {}, error = {}", analysis.getUserId(), ex.getMessage(), ex);
			throw new CustomException(ErrorCode.KAFKA_SEND_FAILURE);
		}
	}
}