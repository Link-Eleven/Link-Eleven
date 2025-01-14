package com.linkeleven.msa.recommendation.infrastructure.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRecommendationRepository {
	private final RedisTemplate<String, Recommendation> redisTemplate;
	private static final String KEY_PREFIX = "recommendation:";

	public void save(Recommendation recommendation) {
		String key = KEY_PREFIX + recommendation.getUserId();
		redisTemplate.opsForValue().set(key, recommendation);
	}
}