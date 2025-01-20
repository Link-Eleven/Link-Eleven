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
public class ChatRoomQueryResponseDto {
	private Long chatRoomId;
	private String chatRoomName;
	private String receiverName;
	private String senderName;

	public static ChatRoomQueryResponseDto from(ChatRoom chatRoom) {
		return ChatRoomQueryResponseDto.builder()
			.chatRoomId(chatRoom.getChatRoomId())
			.chatRoomName(chatRoom.getChatRoomName())
			.receiverName(chatRoom.getReceiver().getUsername())
			.senderName(chatRoom.getSender().getUsername())
			.build();
	}

}
