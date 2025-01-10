package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserIdAndRoleResponseDto {
	private Long userId;
	private String role;

	public static UserIdAndRoleResponseDto of(Long userId, String role) {
		return UserIdAndRoleResponseDto.builder()
			.userId(userId)
			.role(role)
			.build();
	}
}
