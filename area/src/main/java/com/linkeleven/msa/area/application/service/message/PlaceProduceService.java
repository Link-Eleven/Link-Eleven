package com.linkeleven.msa.area.application.service.message;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.area.application.dto.message.PlaceMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceProduceService {
	private final KafkaTemplate<String, Object> kafkaTemplate;


	public void sendPlaceCreateMessage(PlaceMessageDto message) {
		kafkaTemplate.send("place-create-topic", message);
	}
}
