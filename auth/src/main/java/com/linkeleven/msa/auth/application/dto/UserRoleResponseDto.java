package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponseDto {
	private String role;

	public static UserRoleResponseDto from(String role){
		return UserRoleResponseDto.builder()
			.role(role)
			.build();
	}
}
