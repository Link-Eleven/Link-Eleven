package com.linkeleven.msa.feed.application.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private static final String TOPIC = "feed-logs-topic";

	public void sendActivity(Long userId, String title) {
		String message = String.format("userId: %s, title: %s", userId, title);
		log.info("Sending message: {}", message);
		kafkaTemplate.send(TOPIC, String.valueOf(userId), message);
	}

}
