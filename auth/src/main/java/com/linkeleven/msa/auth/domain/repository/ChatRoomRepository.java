package com.linkeleven.msa.auth.domain.repository;

import java.util.Optional;

import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;

public interface ChatRoomRepository {
	Optional<ChatRoom> findById(Long chatRoomId);

	boolean existsBySenderAndReceiver(User sender, User receiver);

	Optional<ChatRoom> findChatRoomByUsers(Long senderId, Long receiverId);

	ChatRoom save(ChatRoom chatRoom);
}
