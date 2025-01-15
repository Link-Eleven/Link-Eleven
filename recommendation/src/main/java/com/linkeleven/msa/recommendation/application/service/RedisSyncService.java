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

import io.lettuce.core.RedisConnectionException;
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

	@Scheduled(fixedDelay = 300000)
	@Transactional
	public void syncRedisFromRdbms() {
		if (!isRedisConnected()) {
			log.warn("Redis 연결이 불안정합니다. 동기화를 건너뜁니다.");
			return;
		}

		int retryCount = 0;
		while (retryCount < MAX_RETRY_COUNT) {
			try {
				log.info("Redis 동기화 시작 (시도 #{}/{})", retryCount + 1, MAX_RETRY_COUNT);
				List<Recommendation> allRecommendations = recommendationRepository.findAll();

				// 동기화 로직
				synchronizeData(allRecommendations);

				log.info("Redis 동기화 완료: {} 건", allRecommendations.size());
				return;

			} catch (RedisConnectionException e) {
				log.error("Redis 연결 오류: {}", e.getMessage());
				retryCount++;
				if (retryCount < MAX_RETRY_COUNT) {
					sleep();
				}
			} catch (Exception e) {
				log.error("Redis 동기화 중 예외 발생: {}", e.getMessage(), e);
				retryCount++;
				if (retryCount < MAX_RETRY_COUNT) {
					sleep();
				}
			}
		}
		log.error("최대 재시도 횟수 초과. Redis 동기화 실패");
	}

	public Boolean isRedisConnected() {
		try {
			String pong = Objects.requireNonNull(redisTemplate.getConnectionFactory())
				.getConnection()
				.ping();
			return pong != null;
		} catch (Exception e) {
			log.warn("Redis 서버 연결 실패: {}", e.getMessage());
			return false;
		}
	}

	private void synchronizeData(List<Recommendation> recommendations) {
		// 기존 캐시 삭제
		Objects.requireNonNull(redisTemplate.getConnectionFactory())
			.getConnection()
			.serverCommands()
			.flushDb(RedisServerCommands.FlushOption.ASYNC);

		// 새로운 데이터 동기화
		for (Recommendation recommendation : recommendations) {
			try {
				RecommendationCache cache = RecommendationCache.from(recommendation);
				String key = "recommendation:" + recommendation.getUserId();
				redisTemplate.opsForValue().set(key, cache);
			} catch (Exception e) {
				log.error("데이터 동기화 중 오류 발생 (ID: {}): {}",
					recommendation.getUserId(), e.getMessage());
				throw e;
			}
		}
	}

	private void sleep() {
		try {
			Thread.sleep(RedisSyncService.RETRY_DELAY_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
