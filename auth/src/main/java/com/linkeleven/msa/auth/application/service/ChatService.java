package com.linkeleven.msa.auth.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.ChatQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.Chat;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.ChatRepository;
import com.linkeleven.msa.auth.domain.repository.ChatRoomRepository;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.jwt.JwtProvider;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;
import com.linkeleven.msa.auth.presentation.dto.ChatSendMessageRequestDto;
import com.linkeleven.msa.auth.presentation.dto.ChatSendMessageResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final UserRepository userRepository;
	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatRepository chatRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final JwtProvider jwtProvider;

	public void sendMessage(
		Long chatRoomId,
		String token,
		ChatSendMessageRequestDto chatSendMessageRequestDto
	) {
		String userId = jwtProvider.validateToken(token);

		User sender = validateUserById(Long.valueOf(userId));
		User receiver = validateUserById(chatSendMessageRequestDto.getReceiverId());

		ChatRoom chatRoom = validateChatRoom(sender.getUserId(), receiver.getUserId(), chatRoomId);

		chatRepository.save(Chat.createChat(chatSendMessageRequestDto.getMessage(), sender, chatRoom));
		messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId,
			ChatSendMessageResponseDto.of(
				chatRoom.getChatRoomId(),
				sender.getUserId(),
				receiver.getUserId(),
				chatRoom.getChatRoomName(),
				chatSendMessageRequestDto.getMessage()
			));

	}

	public Slice<ChatQueryResponseDto> getChatByChatRoomId(Long chatRoomId, Long userId, Pageable pageable) {
		User user = validateUserById(userId);
		ChatRoom chatRoom = validateChatRoomByUserId(user.getUserId(), chatRoomId);
		return chatRepository.findChatByChatRoom(chatRoom, pageable);
	}

	private ChatRoom validateChatRoom(Long senderId, Long receiverId, Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findChatRoomByUsers(senderId, receiverId).orElseThrow(
			() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
		if (!chatRoom.getChatRoomId().equals(chatRoomId)) {
			throw new CustomException(ErrorCode.NOT_YOUR_CHAT_ROOM);
		}
		return chatRoom;
	}

	private User validateUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (user.getDeletedBy() != null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}

	private ChatRoom validateChatRoomByUserId(Long userId, Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findChatRoomByChatRoomIdAndUserId(chatRoomId, userId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_YOUR_CHAT_ROOM));
		if (chatRoom.getDeletedBy() != null) {
			throw new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND);
		}
		return chatRoom;
	}

}
