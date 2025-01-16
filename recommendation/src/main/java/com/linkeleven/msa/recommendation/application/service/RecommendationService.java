package com.linkeleven.msa.recommendation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.recommendation.application.dto.KeywordRecommendationDto;
import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {
	private final RecommendationRepository recommendationRepository;

	@Transactional
	public void processRecommendation(KeywordRecommendationDto recommendationData) {
		saveOrUpdateRecommendation(recommendationData);
	}

	private void saveOrUpdateRecommendation(KeywordRecommendationDto recommendationData) {
		Recommendation recommendation = Recommendation.builder()
			.userId(recommendationData.getUserId())
			.createdBy(recommendationData.getUserId())
			.updatedBy(recommendationData.getUserId())
			.keywords(recommendationData.getKeywordList())
			.build();
		recommendationRepository.saveOrUpdate(recommendation);
	}

	public List<String> getKeywordsForUser(Long userId) {
		Recommendation recommendation = recommendationRepository.findByUserId(userId);
		return recommendation.getKeywords();
	}
}
