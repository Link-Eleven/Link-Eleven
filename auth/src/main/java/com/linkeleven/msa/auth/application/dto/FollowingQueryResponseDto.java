package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowingQueryResponseDto {
	private String username;

	public static FollowingQueryResponseDto from(String username){
		return FollowingQueryResponseDto.builder()
			.username(username)
			.build();
	}
}
