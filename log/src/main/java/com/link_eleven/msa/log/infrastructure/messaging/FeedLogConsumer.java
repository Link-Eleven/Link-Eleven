package com.link_eleven.msa.log.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.link_eleven.msa.log.application.service.FeedLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLogConsumer {
	private final FeedLogService feedLogService;

	@KafkaListener(topics = "feed-logs", groupId = "feed-log-group")
	public void consumeFeedLog(FeedLogMessage message) {
		feedLogService.processNewLog(message.getUserId(), message.getFeedTitle());
	}
}


