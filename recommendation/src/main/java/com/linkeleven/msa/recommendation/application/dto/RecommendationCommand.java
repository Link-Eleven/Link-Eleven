package com.linkeleven.msa.recommendation.application.dto;

import java.util.List;

import com.linkeleven.msa.recommendation.infrastructure.messaging.RecommendationMessage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendationCommand {
	private final Long userId;
	private final List<String> keywords;

	public static RecommendationCommand from(RecommendationMessage message) {
		return RecommendationCommand.builder()
			.userId(message.getUserId())
			.keywords(message.getRecommendationKeywords())
			.build();
	}
}
