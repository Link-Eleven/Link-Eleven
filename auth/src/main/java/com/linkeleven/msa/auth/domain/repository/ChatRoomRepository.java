package com.linkeleven.msa.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.ChatRoomQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;

public interface ChatRoomRepository {
	Optional<ChatRoom> findById(Long chatRoomId);

	boolean existsBySenderAndReceiver(User sender, User receiver);

	Optional<ChatRoom> findChatRoomByUsers(Long senderId, Long receiverId);

	Optional<ChatRoom> findChatRoomByChatRoomIdAndUserId(Long chatRoomId, Long userId);

	ChatRoom save(ChatRoom chatRoom);

	Slice<ChatRoomQueryResponseDto> findChatRoomByQuery(String chatRoomName, String receiverName, Long userId, Pageable pageable);
}
