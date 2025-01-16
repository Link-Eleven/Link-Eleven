package com.linkeleven.msa.auth.presentation.controller.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.service.ChatService;
import com.linkeleven.msa.auth.presentation.dto.ChatSendMessageRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final ChatService chatService;

	@MessageMapping("/chat/{chatRoomId}")
	public void sendMessage(
		@DestinationVariable Long chatRoomId,
		@Header("X-User-Id") Long userId,
		@Payload ChatSendMessageRequestDto sendMessageRequestDto
	) {
		log.info("controller : "+userId);
		chatService.sendMessage(chatRoomId, userId, sendMessageRequestDto);
	}
}
