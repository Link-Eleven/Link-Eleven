package com.linkeleven.msa.notification.application.service;

import java.io.IOException;

import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.CommentEvent;
import com.linkeleven.msa.interaction.LikeEvent;
import com.linkeleven.msa.interaction.ReplyEvent;
import com.linkeleven.msa.notification.domain.model.enums.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationListener {

	private final NotificationService notificationService;

	@RetryableTopic(
		attempts = "5",
		backoff = @Backoff(delay = 3000),
		autoCreateTopics = "false",
		dltTopicSuffix = "_dlt",
		include = {
			org.apache.kafka.common.errors.TimeoutException.class,
			IOException.class
		}
	)
	@KafkaListener(topics = "comment_created", groupId = "notification-group")
	public void listenCommentEvent(@Payload byte[] message) {
		try {
			SpecificDatumReader<CommentEvent> reader = new SpecificDatumReader<>(CommentEvent.class);
			Decoder decoder = DecoderFactory.get().binaryDecoder(message, null);
			CommentEvent event = reader.read(null, decoder);
			notificationService.saveNotificationAndSend(
				NotificationType.COMMENT,
				event.getFeedId(),
				event.getFeedAuthorId(),
				event.getUserId(),
				event.getUsername(),
				event.getEventType()
			);
			log.info("성공 : {}", event.getEventType());
		} catch (IOException e) {
			log.info("직렬화 실패 : {}", e.getMessage());
		}
	}

	@RetryableTopic(
		attempts = "5",
		backoff = @Backoff(delay = 2000),
		autoCreateTopics = "false",
		dltTopicSuffix = "_dlt",
		include = {
			org.apache.kafka.common.errors.TimeoutException.class,
			IOException.class
		}
	)
	@KafkaListener(topics = "reply_created", groupId = "notification-group")
	public void listenReplyEvent(@Payload byte[] message) {
		try {
			SpecificDatumReader<ReplyEvent> reader = new SpecificDatumReader<>(ReplyEvent.class);
			Decoder decoder = DecoderFactory.get().binaryDecoder(message, null);
			ReplyEvent event = reader.read(null, decoder);
			notificationService.saveNotificationAndSend(
				NotificationType.REPLY,
				event.getCommentId(),
				event.getCommentAuthorId(),
				event.getUserId(),
				event.getUsername(),
				event.getEventType()
			);
			log.info("성공 : {}", event.getEventType());
		} catch (IOException e) {
			log.info("직렬화 실패 : {}", e.getMessage());
		}
	}

	@RetryableTopic(
		attempts = "5",
		backoff = @Backoff(delay = 2000),
		autoCreateTopics = "false",
		dltTopicSuffix = "_dlt",
		include = {
			org.apache.kafka.common.errors.TimeoutException.class,
			IOException.class
		}
	)
	@KafkaListener(topics = {"like_created_comment", "like_created_feed", "like_created_reply"}, groupId = "notification-group")
	public void listenLikeEvent(@Payload byte[] message) {
		try {
			SpecificDatumReader<LikeEvent> reader = new SpecificDatumReader<>(LikeEvent.class);
			Decoder decoder = DecoderFactory.get().binaryDecoder(message, null);
			LikeEvent event = reader.read(null, decoder);
			notificationService.saveNotificationAndSend(
				NotificationType.LIKE,
				event.getTargetId(),
				event.getTargetAuthorId(),
				event.getUserId(),
				event.getUsername(),
				event.getContentType()
			);
			log.info("성공 : {}", event.getEventType());
		} catch (IOException e) {
			log.info("직렬화 실패 : {}", e.getMessage());
		}
	}

	@KafkaListener(topics = "comment_created_dlt", groupId = "notification-group")
	public void listenFailedCommentEvent(CommentEvent event) {
		log.error("재시도 실패 후 dlt로 감: {}", event);
	}

	@KafkaListener(topics = "reply_created_dlt", groupId = "notification-group")
	public void listenFailedReplyEvent(ReplyEvent event) {
		log.error("재시도 실패 후 dlt로 감: {}", event);
	}

	@KafkaListener(topics = "like_created_dlt", groupId = "notification-group")
	public void listenFailedLikeEvent(LikeEvent event) {
		log.error("재시도 실패 후 dlt로 감: {}", event);
	}
}
