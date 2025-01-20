package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.Optional;

import com.linkeleven.msa.auth.domain.model.ChatRoom;

public interface ChatRoomQueryRepository {
	Optional<ChatRoom> findChatRoomByUsers(Long senderId, Long receiverId);

}
