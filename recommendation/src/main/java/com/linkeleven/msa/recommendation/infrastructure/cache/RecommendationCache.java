package com.linkeleven.msa.recommendation.infrastructure.cache;

import java.util.List;

import org.springframework.data.redis.core.RedisHash;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationCache {
	private Long userId;
	private List<String> keywords;

	public static RecommendationCache from(Recommendation recommendation) {
		return RecommendationCache.builder()
			.userId(recommendation.getUserId())
			.keywords(recommendation.getKeywords())
			.build();
	}

	public Recommendation toEntity() {
		return Recommendation.builder()
			.userId(this.userId)
			.keywords(this.keywords)
			.build();
	}
}