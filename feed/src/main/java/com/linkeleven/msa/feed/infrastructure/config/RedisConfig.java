package com.linkeleven.msa.feed.infrastructure.config;

import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.linkeleven.msa.feed.domain.model.Feed;

@Configuration
@EnableCaching
public class RedisConfig {

	@Bean
	public RedisTemplate<String, List<Feed>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, List<Feed>> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
}
