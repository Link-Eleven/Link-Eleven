package com.linkeleven.msa.auth.application.dto;

import com.linkeleven.msa.auth.domain.model.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateResponseDto {
	private Long chatRoomId;
	private String senderName;
	private String receiverName;

	public static ChatRoomCreateResponseDto from(ChatRoom chatRoom) {
		return ChatRoomCreateResponseDto.builder()
			.chatRoomId(chatRoom.getChatRoomId())
			.senderName(chatRoom.getSender().getUsername())
			.receiverName(chatRoom.getReceiver().getUsername())
			.build();
	}
}
