package com.linkeleven.msa.recommendation.infrastructure.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

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

	public RecommendationCache findByUserId(Long userId) {
		String key = KEY_PREFIX + userId;
		RecommendationCache cache = redisTemplate.opsForValue().get(key);

		if (cache == null) {
			throw new CustomException(ErrorCode.RECOMMENDATION_NOT_FOUND);
		}
		return cache;
	}

	public void update(Recommendation recommendation) {
		String key = KEY_PREFIX + recommendation.getUserId();
		RecommendationCache keywordsCache = RecommendationCache.from(recommendation);
		redisTemplate.opsForValue().set(key, keywordsCache);
	}
}