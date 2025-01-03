package com.linkeleven.msa.gateway.application.service;

import com.linkeleven.msa.gateway.application.dto.UserRoleResponseDto;

public interface UserService {
	UserRoleResponseDto getUserRole(Long userId);
}
