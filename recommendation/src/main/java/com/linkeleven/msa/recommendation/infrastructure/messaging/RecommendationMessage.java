package com.linkeleven.msa.recommendation.infrastructure.messaging;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationMessage {
	private Long userId;
	private List<String> recommendationKeywords;
}