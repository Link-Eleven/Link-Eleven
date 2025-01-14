package com.linkeleven.msa.recommendation.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.recommendation.application.dto.RecommendationCommand;
import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.RecommendationRepository;
import com.linkeleven.msa.recommendation.infrastructure.configuration.JpaAuditorAware;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {
	private final RecommendationRepository recommendationRepository;
	private final JpaAuditorAware jpaAuditorAware;

	@Transactional
	public void processRecommendation(RecommendationCommand command) {
		jpaAuditorAware.setCurrentAuditor(command.getUserId());

		try {
			saveRecommendation(command);
		} catch (Exception e) {
			log.error("추천 처리 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		} finally {
			jpaAuditorAware.clearCurrentAuditor();
		}
	}

	private void saveRecommendation(RecommendationCommand command) {
		Recommendation recommendation = Recommendation.builder()
			.userId(command.getUserId())
			.keywords(command.getKeywords())
			.build();
		recommendationRepository.save(recommendation);
	}
}
