package com.linkeleven.msa.feed.infrastructure.config;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;

@Configuration
@EnableCaching
public class RedisConfig {

	/**
	 * RedisTemplate을 설정합니다.
	 * RedisConnectionFactory를 통해 Redis 연결을 설정하고,
	 * 키와 값의 직렬화 방법을 지정합니다.
	 * 키는 문자열로, 값은 FeedTopResponseDto의 리스트로 직렬화됩니다. */
	@Bean
	public RedisTemplate<String, List<?>> redisTemplate(
		RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, List<?>> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());

		// ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
			ObjectMapper.DefaultTyping.NON_FINAL);

		// Jackson2JsonRedisSerializer 설정
		Jackson2JsonRedisSerializer<List<FeedTopResponseDto>> serializer = new Jackson2JsonRedisSerializer<>(
			objectMapper.getTypeFactory().constructCollectionType(List.class, FeedTopResponseDto.class)
		);

		template.setValueSerializer(serializer);
		return template;
	}

	@Bean
	public RedisTemplate<String, Object> opsHashRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
}
