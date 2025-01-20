package com.linkeleven.msa.auth.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.linkeleven.msa.auth.application.dto.ChatRoomQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.ChatRoom;

public interface ChatRoomQueryRepository {
	Optional<ChatRoom> findChatRoomByUsers(Long senderId, Long receiverId);

	Optional<ChatRoom> findChatRoomByChatRoomIdAndUserId(Long chatRoomId, Long userId);

	Slice<ChatRoomQueryResponseDto> findChatRoomByQuery(String chatRoomName, String receiverName, Long userId, Pageable pageable);
}
