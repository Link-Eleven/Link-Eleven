package com.linkeleven.msa.recommendation.application.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.RecommendationRepository;
import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;
import com.linkeleven.msa.recommendation.infrastructure.redis.RedisOperations;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSyncService {
	private final RecommendationRepository recommendationRepository;
	private final RedisOperations redisOperations;
	private static final int MAX_RETRY_COUNT = 3;
	private static final long RETRY_DELAY_MS = 1000;
	private static final long FIXED_DELAY = 300000L;

	@Scheduled(fixedDelay = FIXED_DELAY)
	@Transactional
	public void syncRedisFromRdbms() {
		if (!redisOperations.isConnected()) {
			log.warn("Redis 연결이 불안정합니다. 동기화를 중지합니다.");
			throw new CustomException(ErrorCode.REDIS_CONNECTION_ERROR);
		}

		retryWithBackoff(this::performSync);
	}

	private void performSync() {
		List<Recommendation> allRecommendations = recommendationRepository.findAll();
		synchronizeData(allRecommendations);
		log.info("Redis 동기화 완료: {} 건", allRecommendations.size());
	}

	private void synchronizeData(List<Recommendation> recommendations) {
		redisOperations.flushDatabase();
		recommendations.forEach(this::cacheRecommendation);
	}

	private void cacheRecommendation(Recommendation recommendation) {
		RecommendationCache cache = RecommendationCache.from(recommendation);
		String key = "recommendation:" + recommendation.getUserId();
		redisOperations.setValue(key, cache);
	}

	private void retryWithBackoff(Runnable task) {
		int retryCount = 0;

		while (retryCount < MAX_RETRY_COUNT) {
			try {
				task.run();
				return;
			} catch (CustomException e) {
				log.error("오류 발생 (시도 #{}): {}", retryCount + 1, e.getMessage());

				if (retryCount < MAX_RETRY_COUNT - 1) {
					sleep();
				}
				retryCount++;
			}
		}
		throw new CustomException(ErrorCode.REDIS_MAX_RETRY_EXCEEDED);
	}

	private void sleep() {
		try {
			Thread.sleep(RETRY_DELAY_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomException(ErrorCode.REDIS_OPERATION_ERROR);
		}
	}
}
