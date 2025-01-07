package com.linkeleven.msa.auth.application.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.KafkaUpdateUsernameDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaService {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void sendMessage(String topic,KafkaUpdateUsernameDto kafkaDto) {
		kafkaTemplate.send(topic, kafkaDto);
	}
}
