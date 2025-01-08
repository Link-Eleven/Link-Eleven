package com.linkeleven.msa.interaction.application.service.messaging;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.CommentEvent;
import com.linkeleven.msa.interaction.LikeEvent;
import com.linkeleven.msa.interaction.domain.model.entity.OutBox;
import com.linkeleven.msa.interaction.domain.model.enums.EventStatus;
import com.linkeleven.msa.interaction.infrastructure.repository.OutboxRepository;
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
		String topic = generateTopicName(contentType, eventType);
		LikeEvent likeEvent = LikeEvent.newBuilder()
			.setEventType(topic)
			.setTargetId(targetId)
			.setContentType(contentType)
			.setUserId(userId)
			.setLikeTime(likeTime)
			.build();

		AvroSerializer<LikeEvent> avroSerializer = new AvroSerializer<>();
		byte[] serializedPayload = avroSerializer.serialize(likeEvent);
		OutBox outbox = OutBox.create(topic, serializedPayload, EventStatus.PENDING);
		outboxRepository.save(outbox);
	}

	@Transactional
	public void saveCommentCreatedEvent(Long feedId, String content, Long userId,
		String username, String commentCreatedAt) {
		String topic = "comment_created";
		CommentEvent commentEvent = CommentEvent.newBuilder()
			.setEventType(topic)
			.setFeedId(feedId)
			.setContent(content)
			.setUserId(userId)
			.setUsername(username)
			.setCommentCreatedAt(commentCreatedAt)
			.build();

		AvroSerializer<CommentEvent> avroSerializer = new AvroSerializer<>();
		byte[] serializedPayload = avroSerializer.serialize(commentEvent);
		OutBox outbox = OutBox.create(topic, serializedPayload, EventStatus.PENDING);
		outboxRepository.save(outbox);
	}

	private static String generateTopicName(String contentType, String eventType) {
		return String.format("%s_%s", eventType.toLowerCase(), contentType.toLowerCase());
	}
}
