package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaUpdateUsernameDto {
	private Long userId;
	private String username;

	public static KafkaUpdateUsernameDto of(Long userId, String username) {
		return KafkaUpdateUsernameDto.builder()
			.userId(userId)
			.username(username)
			.build();
	}
}
