package com.linkeleven.msa.recommendation.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.recommendation.application.service.FeedLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLogConsumer {
	private final FeedLogService feedLogService;

	@KafkaListener(topics = "feed-logs", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeFeedLog(FeedLogMessage message) {
		feedLogService.processNewLog(message.getUserId(), message.getFeedTitle());
	}
}


