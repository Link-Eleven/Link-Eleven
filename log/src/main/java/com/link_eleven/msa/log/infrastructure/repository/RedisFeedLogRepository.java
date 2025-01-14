package com.link_eleven.msa.log.infrastructure.repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.link_eleven.msa.log.domain.model.FeedLog;
import com.link_eleven.msa.log.domain.repository.FeedLogRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisFeedLogRepository implements FeedLogRepository {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	private static final String KEY_PREFIX = "feedlog:";
	private static final long LIST_MAX_SIZE = 3;

	@Override
	public void save(FeedLog feedLog) {
		try {
			String key = KEY_PREFIX + feedLog.getUserId();
			String value = objectMapper.writeValueAsString(feedLog);

			redisTemplate.opsForList().leftPush(key, value);
			redisTemplate.opsForList().trim(key, 0, LIST_MAX_SIZE - 1);
		} catch (Exception e) {
			throw new RuntimeException("피드 로그 저장에 실패했습니다.", e);
		}
	}

	@Override
	public List<FeedLog> getLatestLogs(Long userId) {
		try {
			String key = KEY_PREFIX + userId;
			List<String> logs = redisTemplate.opsForList().range(key, 0, -1);

			return Objects.requireNonNull(logs).stream()
				.map(log -> {
					try {
						return objectMapper.readValue(log, FeedLog.class);
					} catch (Exception e) {
						throw new RuntimeException("피드 로그 분석에 실패했습니다.", e);
					}
				})
				.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException("피드 로그를 가져오지 못했습니다.", e);
		}
	}
}
