package com.linkeleven.msa.recommendation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.recommendation.application.service.messaging.LogToRecommendationKafkaProducer;
import com.linkeleven.msa.recommendation.domain.model.FeedLog;
import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.FeedLogRepository;
import com.linkeleven.msa.recommendation.infrastructure.client.GeminiClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLogService {
	private final FeedLogRepository feedLogRepository;
	private final GeminiClient geminiClient;
	private final CacheService cacheService;
	private final LogToRecommendationKafkaProducer logToRecommendationKafkaProducer;
	private static final String KEY_PREFIX = "feedlog:";
	private static final int LOG_THRESHOLD = 3;

	public void processNewLog(Long userId, String feedTitle) {
		FeedLog feedLog = createFeedLog(userId, feedTitle);
		saveFeedLog(feedLog);
		processRecommendationIfLogsSufficient(userId);
	}

	private FeedLog createFeedLog(Long userId, String feedTitle) {
		return FeedLog.of(userId, feedTitle);
	}

	private void saveFeedLog(FeedLog feedLog) {
		feedLogRepository.save(feedLog);
	}

	private void processRecommendationIfLogsSufficient(Long userId) {
		List<FeedLog> latestLogs = feedLogRepository.getLatestLogs(userId);
		if (latestLogs.size() >= LOG_THRESHOLD) {
			Recommendation analysis = geminiClient.processLogs(latestLogs);
			logToRecommendationKafkaProducer.sendToRecommendationService(analysis);
			clearUserCache(userId);
		}
	}

	private void clearUserCache(Long userId) {
		String key = KEY_PREFIX + userId;
		cacheService.delete(key);
	}
}