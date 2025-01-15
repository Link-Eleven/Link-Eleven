package com.linkeleven.msa.recommendation.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;

import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisRecommendationRepository {
	private final RedisTemplate<String, RecommendationCache> redisTemplate;
	private static final String KEY_PREFIX = "recommendation:";

	public void save(Recommendation recommendation) {
		try {
			String key = KEY_PREFIX + recommendation.getUserId();
			RecommendationCache cache = RecommendationCache.from(recommendation);
			redisTemplate.opsForValue().set(key, cache);
		} catch (RedisConnectionException e) {
			log.error("Redis 저장 실패: {}", e.getMessage());
		}
	}

	public Optional<RecommendationCache> findByUserId(Long userId) {
		try {
			String key = KEY_PREFIX + userId;
			return Optional.ofNullable(redisTemplate.opsForValue().get(key));
		} catch (RedisConnectionException e) {
			log.error("Redis 조회 실패: {}", e.getMessage());
			return Optional.empty();
		}
	}

	public void update(Recommendation recommendation) {
		String key = KEY_PREFIX + recommendation.getUserId();
		RecommendationCache keywordsCache = RecommendationCache.from(recommendation);
		redisTemplate.opsForValue().set(key, keywordsCache);
	}
}