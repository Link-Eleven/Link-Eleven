package com.linkeleven.msa.auth.presentation.dto;

import lombok.Getter;

@Getter
public class SignInRequestDto {
	private String username;
	private String password;
}
