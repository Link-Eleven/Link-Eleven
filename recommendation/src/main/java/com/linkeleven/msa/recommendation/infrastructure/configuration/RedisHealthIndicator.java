package com.linkeleven.msa.recommendation.infrastructure.configuration;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.recommendation.application.service.RedisSyncService;
import com.linkeleven.msa.recommendation.infrastructure.cache.RecommendationCache;

import io.lettuce.core.RedisConnectionException;

@Component
public class RedisHealthIndicator extends AbstractHealthIndicator {
	private final RedisTemplate<String, RecommendationCache> redisTemplate;
	private final RedisSyncService redisSyncService;

	public RedisHealthIndicator(
		RedisTemplate<String, RecommendationCache> redisTemplate,
		RedisSyncService redisSyncService) {
		this.redisTemplate = redisTemplate;
		this.redisSyncService = redisSyncService;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) {
		try {
			redisTemplate.opsForValue().get("health-check");
			builder.up();
		} catch (RedisConnectionException e) {
			builder.down()
				.withException(e);
			redisSyncService.syncRedisFromRdbms();
		}
	}
}