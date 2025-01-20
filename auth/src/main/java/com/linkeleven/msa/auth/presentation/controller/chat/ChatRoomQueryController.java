package com.linkeleven.msa.auth.presentation.controller.chat;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.auth.application.dto.ChatRoomQueryResponseDto;
import com.linkeleven.msa.auth.application.service.ChatRoomService;
import com.linkeleven.msa.auth.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomQueryController {
	private final ChatRoomService chatRoomService;

	@GetMapping
	public ResponseEntity<SuccessResponseDto<Slice<ChatRoomQueryResponseDto>>> getChatRoomList(
		@RequestParam(required = false)String chatRoomName,
		@RequestParam(required=false)String receiverName,
		@RequestHeader("X-User-Id")String userId,
		Pageable pageable
	){
		return ResponseEntity.ok().body(SuccessResponseDto.success(
			"채팅방 조회가 되었습니다.",
			chatRoomService.getChatRoomList(chatRoomName,receiverName,Long.valueOf(userId),pageable)
		));
	}
}
