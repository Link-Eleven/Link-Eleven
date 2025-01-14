package com.link_eleven.msa.log.infrastructure.messaging;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.link_eleven.msa.log.domain.model.GeminiKeywordHolder;
import com.link_eleven.msa.log.libs.exception.CustomException;
import com.link_eleven.msa.log.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationProducer {
	private final KafkaTemplate<String, RecommendationMessage> kafkaTemplate;
	private static final String TOPIC = "user-log-keywords";

	public void sendToRecommendationService(GeminiKeywordHolder analysis) {
		RecommendationMessage message = RecommendationMessage.builder()
			.userId(analysis.getUserId())
			.keywords(analysis.getKeywords())
			.build();

		log.info("RecommendationMessage 생성 완료 - userId: {}, keywords: {}",
			message.getUserId(), message.getKeywords());

		CompletableFuture<SendResult<String, RecommendationMessage>> future =
			kafkaTemplate.send(TOPIC, message);

		future.thenAccept(result ->
			log.info("사용자에 대한 키워드를 성공적으로 보냈습니다.: {}", analysis.getUserId())
		).exceptionally(ex -> {
			log.error("키워드 전송에 실패했습니다", ex);
			throw new CustomException(ErrorCode.KAFKA_SEND_FAILURE);
		});
	}
}