package com.linkeleven.msa.interaction.application.service.messaging;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.domain.model.entity.OutBox;
import com.linkeleven.msa.interaction.domain.model.enums.EventStatus;
import com.linkeleven.msa.interaction.infrastructure.messaging.KafkaProducerService;
import com.linkeleven.msa.interaction.infrastructure.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxProcessorService {

	private final OutboxRepository outboxRepository;
	private final KafkaProducerService kafkaProducerService;
	private final OutboxStatusChangeService outboxStatusChangeService;

	@Scheduled(fixedDelay = 10000)
	public void sendPendingEvent() {
		List<OutBox> pendingEventList = outboxRepository.findByEventStatus(EventStatus.PENDING);

		for (OutBox outBox : pendingEventList) {
			CompletableFuture<SendResult<String, Object>> future = kafkaProducerService.sendEvent(outBox);

			future.whenComplete((result, throwable) -> {
				if (throwable == null) {
					outboxStatusChangeService.updateStatusProcessed(outBox);
				} else {
					outboxStatusChangeService.updateStatusFailed(outBox);
				}
			});
		}
	}
}
