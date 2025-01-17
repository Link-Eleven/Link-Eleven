package com.linkeleven.msa.auth.presentation.dto;

import com.linkeleven.msa.auth.domain.common.UserRole;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
	private String username;
	private String password;
	private Boolean isAnonymous;
	private UserRole role;

}
