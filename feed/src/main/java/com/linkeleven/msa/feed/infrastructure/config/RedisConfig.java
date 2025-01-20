package com.linkeleven.msa.feed.infrastructure.config;

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

	@Bean
	public RedisTemplate<String, FeedTopResponseDto> redisTemplate(
		RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, FeedTopResponseDto> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		Jackson2JsonRedisSerializer<FeedTopResponseDto> serializer = new Jackson2JsonRedisSerializer<>(
			FeedTopResponseDto.class);

		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);

		return template;
	}

	@Bean
	public RedisTemplate<String, Object> opsHashRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

		template.setHashValueSerializer(serializer);
		template.setValueSerializer(serializer);

		return template;
	}
}
