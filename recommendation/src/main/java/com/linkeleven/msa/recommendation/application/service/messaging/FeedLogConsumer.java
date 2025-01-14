package com.linkeleven.msa.recommendation.application.service.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.recommendation.application.service.FeedLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedLogConsumer {
	private final FeedLogService feedLogService;

	@KafkaListener(topics = "feed-logs", groupId = "${spring.kafka.consumer.group-id}")
	@RetryableTopic(
		attempts = "3",
		backoff = @Backoff(delay = 2000)
	)
	public void consumeFeedLog(FeedLogMessage message) {
		try {
			feedLogService.processNewLog(message.getUserId(), message.getFeedTitle());
			log.info("메시지가 성공적으로 처리되었습니다.: {}", message);
		} catch (Exception e) {
			log.error("메시지 처리 오류: {}", message, e);
			throw e;
		}
	}
}


