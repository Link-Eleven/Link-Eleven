package com.linkeleven.msa.auth.presentation.controller.chat;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.ChatQueryResponseDto;
import com.linkeleven.msa.auth.application.service.ChatService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats/")
public class ChatQueryController {
	private final ChatService chatService;

	@GetMapping("/{chatRoomId}")
	public ResponseEntity<SuccessResponseDto<Slice<ChatQueryResponseDto>>> getChatByChatRoomId(
		@PathVariable("chatRoomId") Long chatRoomId,
		@RequestHeader("X-User-Id") String userId,
		Pageable pageable
	) {
		return ResponseEntity.ok().body(
			SuccessResponseDto.success(
				"채팅방 채팅이 조회 되었습니다.",
				chatService.getChatByChatRoomId(chatRoomId, Long.valueOf(userId),pageable)
				)
		);

	}
}
