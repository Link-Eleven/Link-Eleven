package com.linkeleven.msa.recommendation.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.recommendation.domain.model.Recommend;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationProducer {
	private final KafkaTemplate<String, RecommendationMessage> kafkaTemplate;
	private static final String TOPIC = "log.keywords";

	public void sendToRecommendationService(Recommend analysis) {
		RecommendationMessage message = RecommendationMessage.builder()
			.userId(analysis.getUserId())
			.keywords(analysis.getKeywords())
			.build();

		kafkaTemplate.send(TOPIC, message)
			.whenComplete((result, ex) -> {
				if (ex == null) {
					log.info("사용자에 대한 키워드를 성공적으로 보냈습니다.: {}", analysis.getUserId());
				} else {
					log.error("키워드 전송에 실패했습니다", ex);
					throw new CustomException(ErrorCode.KAFKA_SEND_FAILURE);
				}
			});
	}
}