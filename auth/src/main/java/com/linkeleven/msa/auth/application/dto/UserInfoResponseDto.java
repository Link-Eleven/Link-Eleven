package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
	private String username;
	public static UserInfoResponseDto from(String username) {
		return UserInfoResponseDto.builder()
			.username(username)
			.build();
	}
}
