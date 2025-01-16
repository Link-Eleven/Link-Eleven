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

	@Bean
	public RedisTemplate<String, List<FeedTopResponseDto>> redisTemplate(
		RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, List<FeedTopResponseDto>> template = new RedisTemplate<>();
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
