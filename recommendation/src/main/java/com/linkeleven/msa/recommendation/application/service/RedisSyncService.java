package com.linkeleven.msa.recommendation.application.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.recommendation.domain.model.Recommendation;
import com.linkeleven.msa.recommendation.domain.repository.RecommendationRepository;
import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSyncService {
	private final RecommendationRepository recommendationRepository;
	private final RedisTemplate<String, RecommendationCache> redisTemplate;
	private static final int MAX_RETRY_COUNT = 3;
	private static final long RETRY_DELAY_MS = 1000;
	private static final long FIXED_DELAY = 300000L;

	@Scheduled(fixedDelay = FIXED_DELAY)
	@Transactional
	public void syncRedisFromRdbms() {
		if (!isRedisConnected()) {
			log.warn("Redis 연결이 불안정합니다. 동기화를 중지합니다.");
			return;
		}

		retryWithBackoff(this::performSync);
	}

	private void performSync() {
		List<Recommendation> allRecommendations = recommendationRepository.findAll();
		synchronizeData(allRecommendations);
		log.info("Redis 동기화 완료: {} 건", allRecommendations.size());
	}

	private Boolean isRedisConnected() {
		try {
			String pong = Objects.requireNonNull(redisTemplate.getConnectionFactory())
				.getConnection()
				.ping();
			return "PONG".equals(pong);
		} catch (Exception e) {
			log.warn("Redis 서버 연결 실패: {}", e.getMessage());
			return false;
		}
	}

	private void synchronizeData(List<Recommendation> recommendations) {
		flushRedisCache();
		for (Recommendation recommendation : recommendations) {
			cacheRecommendation(recommendation);
		}
	}

	private void flushRedisCache() {
		try {
			Objects.requireNonNull(redisTemplate.getConnectionFactory())
				.getConnection()
				.serverCommands()
				.flushDb(RedisServerCommands.FlushOption.ASYNC);
		} catch (Exception e) {
			log.error("Redis 캐시 플러시 중 오류 발생: {}", e.getMessage());
			throw e;
		}
	}

	private void cacheRecommendation(Recommendation recommendation) {
		try {
			RecommendationCache cache = RecommendationCache.from(recommendation);
			String key = "recommendation:" + recommendation.getUserId();
			redisTemplate.opsForValue().set(key, cache);
		} catch (Exception e) {
			log.error("데이터 동기화 중 오류 발생 (ID: {}): {}", recommendation.getUserId(), e.getMessage());
			throw e;
		}
	}

	private void retryWithBackoff(Runnable task) {
		int retryCount = 0;
		while (retryCount < MAX_RETRY_COUNT) {
			try {
				task.run();
				return;
			} catch (Exception e) {
				handleRetry(retryCount++, e);
			}
		}
		log.error("최대 재시도 횟수 초과. Redis 동기화 실패");
	}

	private void handleRetry(int retryCount, Exception e) {
		log.error("오류 발생 (시도 #{}): {}", retryCount + 1, e.getMessage(), e);
		if (retryCount < MAX_RETRY_COUNT - 1) {
			sleep();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(RETRY_DELAY_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
