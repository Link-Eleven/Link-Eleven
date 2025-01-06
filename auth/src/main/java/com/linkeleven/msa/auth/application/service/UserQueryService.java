package com.linkeleven.msa.auth.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.auth.application.dto.UserQueryResponseDto;
import com.linkeleven.msa.auth.domain.repository.UserRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
	private final UserRepositoryCustom userRepository;

	public Slice<UserQueryResponseDto> getUsersByUsername(String username, Pageable pageable) {
		return userRepository.findUserByUsername(username, pageable);
	}
}
