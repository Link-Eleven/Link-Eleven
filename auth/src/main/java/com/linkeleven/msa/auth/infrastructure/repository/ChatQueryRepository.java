package com.linkeleven.msa.auth.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.ChatQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.ChatRoom;

public interface ChatQueryRepository {
	Slice<ChatQueryResponseDto> findChatByChatRoom(ChatRoom chatRoom, Pageable pageable);
}
