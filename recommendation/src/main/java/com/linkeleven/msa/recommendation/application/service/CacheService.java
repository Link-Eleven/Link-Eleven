package com.linkeleven.msa.recommendation.application.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CacheService {
	private final RedisTemplate<String, String> redisTemplate;

	public void delete(String key) {
		redisTemplate.delete(key);
	}
}
