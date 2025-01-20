package com.linkeleven.msa.auth.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.ChatQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.Chat;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;

public interface ChatRepository {
	Chat save(Chat chat);

	boolean existsBySenderAndChatRoom(User sender, ChatRoom chatRoom);

	Slice<ChatQueryResponseDto> findChatByChatRoom(ChatRoom chatRoom, Pageable pageable);
}
