package com.linkeleven.msa.auth.presentation.dto;

import lombok.Getter;

@Getter
public class ChatSendMessageRequestDto {
	private Long receiverId;
	private String message;
}
