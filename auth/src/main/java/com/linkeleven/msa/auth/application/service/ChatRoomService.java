package com.linkeleven.msa.auth.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.auth.application.dto.ChatRoomCreateResponseDto;
import com.linkeleven.msa.auth.application.dto.ChatRoomQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;
import com.linkeleven.msa.auth.domain.repository.ChatRoomRepository;
import com.linkeleven.msa.auth.domain.repository.UserRepository;
import com.linkeleven.msa.auth.libs.exception.CustomException;
import com.linkeleven.msa.auth.libs.exception.ErrorCode;
import com.linkeleven.msa.auth.presentation.dto.ChatRoomCreateRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;

	public ChatRoomCreateResponseDto createChatRoom(
		ChatRoomCreateRequestDto chatRoomCreateRequestDto,
		Long userId
	) {
		User sender = validateUserById(userId);
		User receiver = validateUserById(chatRoomCreateRequestDto.getReceiverId());

		String chatRoomName = validateChatRoomName(chatRoomCreateRequestDto.getChatRoomName(), receiver.getUsername());
		validateChatRoom(sender, receiver);

		return ChatRoomCreateResponseDto.from(
			chatRoomRepository.save(ChatRoom.createChatRoom(chatRoomName, sender, receiver)));
	}

	public Slice<ChatRoomQueryResponseDto> getChatRoomList(
		String chatRoomName,
		String receiverName,
		Long userId,
		Pageable pageable
	) {
		User user = validateUserById(userId);
		if(receiverName!=null) {
			User receiver = validateUserByName(receiverName);
		}
		return chatRoomRepository.findChatRoomByQuery(chatRoomName, receiverName, user.getUserId(), pageable);
	}

	private User validateUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (user.getDeletedBy() != null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}

	private User validateUserByName(String receiverName) {
		User user = userRepository.findByUsername(receiverName).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (user.getDeletedBy() != null) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}

	private String validateChatRoomName(String chatRoomName, String receiverUsername) {
		if (chatRoomName != null) {
			validateRoomNameLength(chatRoomName);
			return chatRoomName;
		}
		return receiverUsername;
	}

	private void validateRoomNameLength(String chatRoomName) {
		if (chatRoomName.length() > 10) {
			throw new CustomException(ErrorCode.CHATROOM_NAME_ERROR);
		}
	}

	private void validateChatRoom(User sender, User receiver) {
		if (chatRoomRepository.existsBySenderAndReceiver(sender, receiver)) {
			throw new CustomException(ErrorCode.ALREADY_CHATROOM);
		}
		if (chatRoomRepository.existsBySenderAndReceiver(receiver, sender)) {
			throw new CustomException(ErrorCode.ALREADY_CHATROOM);
		}
	}

}
