package com.linkeleven.msa.feed.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.feed.infrastructure.client.RecommendationClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

	private final RecommendationClient recommendationClient;

	public List<String> getRecommendedKeywords(Long userId, List<String> KeywordList) {
		List<String> recommendedKeywords = recommendationClient.getRecommendationKeywords(userId);
		if (KeywordList == null) {
			return recommendedKeywords;
		} else {
			KeywordList.addAll(recommendedKeywords);
			return KeywordList;
		}
	}

}
