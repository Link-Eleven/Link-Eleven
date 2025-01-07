package com.linkeleven.msa.interaction.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.LikeEvent;
import com.linkeleven.msa.interaction.domain.model.entity.OutBox;
import com.linkeleven.msa.interaction.domain.model.enums.EventStatus;
import com.linkeleven.msa.interaction.domain.repository.OutboxRepository;
import com.linkeleven.msa.interaction.infrastructure.util.AvroSerializer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxService {

	private final OutboxRepository outboxRepository;

	@Transactional
	public void saveLikeCreatedEvent(Long targetId, String contentType, Long userId,
		String likeTime, String eventType) {
		LikeEvent likeEvent = LikeEvent.newBuilder()
			.setEventType("LIKE_CREATED")
			.setTargetId(targetId)
			.setContentType(contentType)
			.setUserId(userId)
			.setLikeTime(likeTime)
			.build();

		AvroSerializer<LikeEvent> avroSerializer = new AvroSerializer<>();
		byte[] serializedPayload = avroSerializer.serialize(likeEvent);
		OutBox outbox = OutBox.create(eventType, serializedPayload, EventStatus.PENDING);
		outboxRepository.save(outbox);
	}
}
