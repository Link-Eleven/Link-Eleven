package com.linkeleven.msa.interaction.application.service.messaging;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.domain.model.entity.OutBox;
import com.linkeleven.msa.interaction.infrastructure.repository.OutboxRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OutboxStatusChangeService {

	private final OutboxRepository outboxRepository;

	public void updateStatusProcessed(OutBox outBox) {
		outBox.updateStatusProcessed();
		outboxRepository.save(outBox);
	}

	public void updateStatusFailed(OutBox outBox) {
		outBox.updateStatusFailed();
		outboxRepository.save(outBox);
	}
}
