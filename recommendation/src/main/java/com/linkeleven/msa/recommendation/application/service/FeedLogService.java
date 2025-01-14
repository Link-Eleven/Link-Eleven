package com.linkeleven.msa.recommendation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.recommendation.domain.model.FeedLog;
import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.FeedLogRepository;
import com.linkeleven.msa.recommendation.infrastructure.client.GeminiClient;
import com.linkeleven.msa.recommendation.infrastructure.messaging.LogToRecommendationServiceProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLogService {
	private final FeedLogRepository feedLogRepository;
	private final GeminiClient geminiClient;
	private final LogToRecommendationServiceProducer logToRecommendationServiceProducer;

	public void processNewLog(Long userId, String feedTitle) {
		FeedLog feedLog = FeedLog.of(userId, feedTitle);
		feedLogRepository.save(feedLog);

		List<FeedLog> latestLogs = feedLogRepository.getLatestLogs(userId);
		if (latestLogs.size() >= 3) {
			Recommendation analysis = geminiClient.processLogs(latestLogs);
			logToRecommendationServiceProducer.sendToRecommendationService(analysis);
		}
	}
}