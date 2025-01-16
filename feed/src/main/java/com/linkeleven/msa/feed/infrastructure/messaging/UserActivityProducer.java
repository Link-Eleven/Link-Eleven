package com.linkeleven.msa.feed.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.feed.application.dto.UserActivityMessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityProducer {

	private final KafkaTemplate<String, UserActivityMessageDto> kafkaTemplate;
	private static final String TOPIC = "feed-logs-topic";

	public void sendActivity(Long userId, String title) {
		UserActivityMessageDto messageDto = UserActivityMessageDto.builder()
			.userId(userId)
			.title(title)
			.build();
		log.info("Sending message: {}", messageDto);
		kafkaTemplate.send(TOPIC, String.valueOf(userId), messageDto);
	}

}
