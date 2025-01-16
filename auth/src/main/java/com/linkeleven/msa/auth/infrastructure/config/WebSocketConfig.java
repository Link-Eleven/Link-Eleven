package com.linkeleven.msa.auth.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 웹소켓이 handshake를 하기 위해 연결하는 endpoint
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 클라이언트 -> 서버로 발행하는 메세지 보냄
		config.setApplicationDestinationPrefixes("/pub");

		// 서버 -> 클라이언트로 발행하는 메세지에 대한 endpoint 설정
		config.enableSimpleBroker("/sub");
	}

}
