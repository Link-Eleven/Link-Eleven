package com.linkeleven.msa.auth.presentation.dto;

import lombok.Getter;

@Getter
public class ChatRoomCreateRequestDto {
	private String chatRoomName;
	private Long receiverId;
}
