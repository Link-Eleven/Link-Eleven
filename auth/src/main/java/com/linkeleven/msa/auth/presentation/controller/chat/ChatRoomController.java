package com.linkeleven.msa.auth.presentation.controller.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.ChatRoomCreateResponseDto;
import com.linkeleven.msa.auth.application.service.ChatRoomService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.auth.presentation.dto.ChatRoomCreateRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;

	@PostMapping
	public ResponseEntity<SuccessResponseDto<ChatRoomCreateResponseDto>> createChatRoom(
		@RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto,
		@RequestHeader("X-User-Id") String userId
	) {
		return ResponseEntity.ok().body(
			SuccessResponseDto.success(
				"채팅방이 생성되었습니다.",
				chatRoomService.createChatRoom(chatRoomCreateRequestDto, Long.valueOf(userId))
			));
	}
}
