package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatQueryResponseDto {
	private String senderName;
	private String message;

	public static ChatQueryResponseDto of(String senderName, String message) {
		return ChatQueryResponseDto.builder()
			.senderName(senderName)
			.message(message)
			.build();
	}

}
