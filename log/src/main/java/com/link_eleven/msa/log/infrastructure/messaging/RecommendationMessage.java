package com.link_eleven.msa.log.infrastructure.messaging;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendationMessage {
	private Long userId;
	private List<String> keywords;
}