package com.linkeleven.msa.area.application.service.message;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.area.application.dto.message.PlaceMessageDto;

@Service
public class PlaceProduceService {

	private final KafkaTemplate<String, PlaceMessageDto> kafkaTemplate;

	public PlaceProduceService(KafkaTemplate kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendPlaceCreateMessage(PlaceMessageDto message) {
		kafkaTemplate.send("place-create-topic", message);
	}
}
