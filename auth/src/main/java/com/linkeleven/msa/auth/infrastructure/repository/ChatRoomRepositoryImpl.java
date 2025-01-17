package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final ChatRoomQueryRepository chatRoomQueryRepository;

	@Override
	public Optional<ChatRoom> findById(Long chatRoomId) {
		return chatRoomJpaRepository.findById(chatRoomId);
	}
	@Override
	public boolean existsBySenderAndReceiver(User sender, User receiver) {
		return chatRoomJpaRepository.existsBySenderAndReceiver(sender, receiver);
	}
	@Override
	public Optional<ChatRoom> findChatRoomByUsers(Long senderId, Long receiverId){
		return chatRoomQueryRepository.findChatRoomByUsers(senderId, receiverId);
	}

	@Override
	public ChatRoom save(ChatRoom chatRoom){
		return chatRoomJpaRepository.save(chatRoom);
	}
}
