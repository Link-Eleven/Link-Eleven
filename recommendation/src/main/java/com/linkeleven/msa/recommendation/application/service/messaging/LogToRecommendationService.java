package com.linkeleven.msa.recommendation.application.service.messaging;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.infrastructure.messaging.LogToRecommendationServiceProducer;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogToRecommendationService {

	private final LogToRecommendationServiceProducer producer;

	public void sendRecommendation(Recommendation analysis) {
		log.info("추천 키워드 전송 시작: userId={}", analysis.getUserId());

		try {
			producer.sendToRecommendationService(analysis);
			log.info("추천 키워드 전송 성공: userId={}", analysis.getUserId());
		} catch (Exception ex) {
			log.error("추천 키워드 전송 실패: userId={}, error={}", analysis.getUserId(), ex.getMessage());
			throw new CustomException(ErrorCode.KAFKA_SEND_FAILURE);
		}
	}
}