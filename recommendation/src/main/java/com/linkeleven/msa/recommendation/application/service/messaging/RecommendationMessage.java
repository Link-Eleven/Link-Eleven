package com.linkeleven.msa.recommendation.application.service.messaging;

import java.time.LocalDateTime;
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
	private LocalDateTime timestamp;
}