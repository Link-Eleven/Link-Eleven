package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateUsernameResponseDto {
	private Long userId;
	private String username;

	public static UserUpdateUsernameResponseDto of(Long userId,String username) {
		return UserUpdateUsernameResponseDto.builder()
			.userId(userId)
			.username(username)
			.build();

	}
}
