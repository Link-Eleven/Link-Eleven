package com.linkeleven.msa.recommendation.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkeleven.msa.recommendation.domain.model.FeedLog;
import com.linkeleven.msa.recommendation.domain.repository.FeedLogRepository;
import com.linkeleven.msa.recommendation.libs.exception.CustomException;
import com.linkeleven.msa.recommendation.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisFeedLogRepository implements FeedLogRepository {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	private static final String KEY_PREFIX = "feedlog:";
	private static final long LIST_MAX_SIZE = 3;

	@Override
	public void save(FeedLog feedLog) {
		String key = KEY_PREFIX + feedLog.getUserId();
		String value = serializeFeedLog(feedLog);

		var listOps = redisTemplate.opsForList();
		listOps.leftPush(key, value);
		listOps.trim(key, 0, LIST_MAX_SIZE - 1);
	}

	private String serializeFeedLog(FeedLog feedLog) {
		try {
			return objectMapper.writeValueAsString(feedLog);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FEED_LOG_SAVE_ERROR);
		}
	}

	@Override
	public List<FeedLog> getLatestLogs(Long userId) {
		String key = KEY_PREFIX + userId;
		List<String> logs = redisTemplate.opsForList().range(key, 0, -1);

		if (logs == null) {
			return List.of();
		}

		return logs.stream()
			.map(this::deserializeFeedLog)
			.collect(Collectors.toList());
	}

	private FeedLog deserializeFeedLog(String log) {
		try {
			return objectMapper.readValue(log, FeedLog.class);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FEED_LOG_PARSE_ERROR);
		}
	}
}