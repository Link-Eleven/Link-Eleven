package com.linkeleven.msa.feed.infrastructure.messaging;

import org.springframework.kafka.KafkaException;
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

		try {
			kafkaTemplate.send(TOPIC, String.valueOf(userId), messageDto);
		} catch (KafkaException e) {
			resend(userId, messageDto, 0);
			log.error("Kafka에 메시지 전송 실패: {}", e.getMessage());
		}
	}

	private void resend(Long userId, UserActivityMessageDto messageDto, int retryCount) {
		try {
			Thread.sleep(1000);
			kafkaTemplate.send(TOPIC, String.valueOf(userId), messageDto);
		} catch (KafkaException | InterruptedException e) {
			if (retryCount < 3) {
				resend(userId, messageDto, retryCount + 1);
			}
		}
	}

}
