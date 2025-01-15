package com.linkeleven.msa.recommendation.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRecommendationRepository {
	private final RedisTemplate<String, RecommendationCache> redisTemplate;
	private static final String KEY_PREFIX = "recommendation:";

	public void save(Recommendation recommendation) {
		String key = KEY_PREFIX + recommendation.getUserId();
		RecommendationCache keywordsCache = RecommendationCache.from(recommendation);
		redisTemplate.opsForValue().set(key, keywordsCache);
	}

	public Optional<RecommendationCache> findByUserId(Long userId) {
		String key = KEY_PREFIX + userId;
		RecommendationCache keywordsCache = redisTemplate.opsForValue().get(key);
		return Optional.ofNullable(keywordsCache);
	}

	public void update(Recommendation recommendation) {
		String key = KEY_PREFIX + recommendation.getUserId();
		RecommendationCache keywordsCache = RecommendationCache.from(recommendation);
		redisTemplate.opsForValue().set(key, keywordsCache);
	}
}