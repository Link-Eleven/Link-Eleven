package com.link_eleven.msa.log.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.link_eleven.msa.log.domain.model.FeedLog;
import com.link_eleven.msa.log.domain.model.GeminiKeywordHolder;
import com.link_eleven.msa.log.domain.repository.FeedLogRepository;
import com.link_eleven.msa.log.infrastructure.client.GeminiClient;
import com.link_eleven.msa.log.infrastructure.messaging.RecommendationProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLogService {
	private final FeedLogRepository feedLogRepository;
	private final GeminiClient geminiClient;
	private final RecommendationProducer recommendationProducer;

	public void processNewLog(Long userId, String feedTitle) {
		FeedLog feedLog = FeedLog.of(userId, feedTitle);
		feedLogRepository.save(feedLog);

		List<FeedLog> latestLogs = feedLogRepository.getLatestLogs(userId);
		if (latestLogs.size() >= 3) {
			GeminiKeywordHolder analysis = geminiClient.processLogs(latestLogs);
			recommendationProducer.sendToRecommendationService(analysis);
		}
	}
}