package com.linkeleven.msa.recommendation.infrastructure.redis;

import java.util.Objects;

import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisOperations {
	private final RedisTemplate<String, RecommendationCache> redisTemplate;
	private static final String REDIS_PONG_RESPONSE = "PONG";

	public boolean isConnected() {
		try {
			String pong = Objects.requireNonNull(redisTemplate.getConnectionFactory())
				.getConnection()
				.ping();
			return REDIS_PONG_RESPONSE.equalsIgnoreCase(pong);
		} catch (Exception e) {
			log.error("Redis 연결 확인 중 오류 발생: {}", e.getMessage());
			return false;
		}
	}

	public void flushDatabase() {
		try {
			Objects.requireNonNull(redisTemplate.getConnectionFactory())
				.getConnection()
				.serverCommands()
				.flushDb(RedisServerCommands.FlushOption.ASYNC);
		} catch (Exception e) {
			log.error("Redis 데이터베이스 플러시 중 오류 발생: {}", e.getMessage());
			throw new CustomException(ErrorCode.REDIS_OPERATION_ERROR);
		}
	}

	public void setValue(String key, RecommendationCache value) {
		redisTemplate.opsForValue().set(key, value);
	}

}


