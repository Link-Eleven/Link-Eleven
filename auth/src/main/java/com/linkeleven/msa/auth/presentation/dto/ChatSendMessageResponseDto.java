package com.linkeleven.msa.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSendMessageResponseDto {
	private Long chatRoomId;
	private Long senderId;
	private Long receiverId;
	private String chatRoomName;
	private String message;

	public static ChatSendMessageResponseDto of(Long chatRoomId, Long senderId, Long receiverId,String chatRoomName, String message) {
		return ChatSendMessageResponseDto.builder()
			.chatRoomId(chatRoomId)
			.senderId(senderId)
			.receiverId(receiverId)
			.chatRoomName(chatRoomName)
			.message(message)
			.build();
	}
}
