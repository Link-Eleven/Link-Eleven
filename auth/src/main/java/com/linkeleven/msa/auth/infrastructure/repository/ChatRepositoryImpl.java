package com.linkeleven.msa.auth.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.domain.model.Chat;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
	private final ChatJpaRepository chatJpaRepository;

	@Override
	public Chat save(Chat chat) {
		return chatJpaRepository.save(chat);
	}

	@Override
	public boolean existsBySenderAndChatRoom(User sender, ChatRoom chatRoom) {
		return chatJpaRepository.existsBySenderAndChatRoom(sender, chatRoom);
	}

}
