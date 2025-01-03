package com.linkeleven.msa.auth.application.dto;

import com.linkeleven.msa.auth.domain.common.UserRole;
import com.linkeleven.msa.auth.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMyInfoResponseDto {
	private Long userId;
	private String username;
	private UserRole role;
	private boolean isAnonymous;
	private boolean isCouponIssued;

	public static UserMyInfoResponseDto from(User user) {
		return UserMyInfoResponseDto.builder()
			.userId(user.getUserId())
			.username(user.getUsername())
			.role(user.getRole())
			.isAnonymous(user.isAnonymous())
			.isCouponIssued(user.isCouponIssued())
			.build();
	}

}
