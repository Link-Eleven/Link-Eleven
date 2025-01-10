package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowingUsernameResponseDto {
	private String followingName;

	public static FollowingUsernameResponseDto from(String followingName) {
		return FollowingUsernameResponseDto.builder()
			.followingName(followingName)
			.build();
	}
}
