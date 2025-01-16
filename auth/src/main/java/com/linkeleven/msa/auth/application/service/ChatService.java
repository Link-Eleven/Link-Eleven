package com.linkeleven.msa.auth.application.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.domain.model.Chat;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.ChatRepository;
import com.linkeleven.msa.auth.domain.repository.ChatRoomRepository;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
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

	public void sendMessage(
		Long chatRoomId,
		Long userId,
		ChatSendMessageRequestDto chatSendMessageRequestDto
	) {
		User sender = validateUserById(userId);
		User receiver = validateUserById(chatSendMessageRequestDto.getReceiverId());


		ChatRoom chatRoom = validateChatRoom(sender.getUserId(), receiver.getUserId(), chatRoomId);
		log.info("message : "+chatSendMessageRequestDto.getMessage());
		chatRepository.save(Chat.createChat(chatSendMessageRequestDto.getMessage(),sender,chatRoom));
		messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId,
			ChatSendMessageResponseDto.of(
				chatRoom.getChatRoomId(),
				sender.getUserId(),
				receiver.getUserId(),
				chatRoom.getChatRoomName(),
				chatSendMessageRequestDto.getMessage()
			));

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
}
