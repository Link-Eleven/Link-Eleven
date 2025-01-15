package com.linkeleven.msa.recommendation.application.dto;

import java.util.List;

import com.linkeleven.msa.recommendation.application.service.messaging.RecommendationMessage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordRecommendationDto {
	private final Long userId;
	private final List<String> keywords;

	public static KeywordRecommendationDto from(RecommendationMessage message) {
		return KeywordRecommendationDto.builder()
			.userId(message.getUserId())
			.keywords(message.getRecommendationKeywords())
			.build();
	}
}
