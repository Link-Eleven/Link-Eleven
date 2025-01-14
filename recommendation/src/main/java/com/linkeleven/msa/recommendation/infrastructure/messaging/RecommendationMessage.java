package com.linkeleven.msa.recommendation.infrastructure.messaging;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendationMessage {
	private Long userId;
	private List<String> recommendationKeywords;
}