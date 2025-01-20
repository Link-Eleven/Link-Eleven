package com.linkeleven.msa.auth.infrastructure.repository;

import static com.linkeleven.msa.auth.domain.model.QChatRoom.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.ChatRoomQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<ChatRoom> findChatRoomByUsers(Long senderId, Long receiverId) {
		ChatRoom room = queryFactory.selectFrom(chatRoom)
			.from(chatRoom)
			.where(
				(chatRoom.sender.userId.eq(senderId).and(chatRoom.receiver.userId.eq(receiverId)))
					.or(chatRoom.sender.userId.eq(receiverId).and(chatRoom.receiver.userId.eq(senderId)))
			)
			.fetchOne();  // 결과를 단 하나의 ChatRoom으로 가져옵니다.
		return Optional.ofNullable(room);
	}

	@Override
	public Optional<ChatRoom> findChatRoomByChatRoomIdAndUserId(Long chatRoomId, Long userId) {
		ChatRoom room = queryFactory.selectFrom(chatRoom)
			.from(chatRoom)
			.where(
				chatRoom.chatRoomId.eq(chatRoomId).and(chatRoom.sender.userId.eq(userId))
					.or(chatRoom.chatRoomId.eq(chatRoomId).and(chatRoom.receiver.userId.eq(userId)))
			)
			.fetchOne();
		return Optional.ofNullable(room);
	}

	@Override
	public Slice<ChatRoomQueryResponseDto> findChatRoomByQuery(
		String chatRoomName, String receiverName, Long userId, Pageable pageable
	) {

		List<ChatRoomQueryResponseDto> roomList = queryFactory.select(
				Projections.constructor(ChatRoomQueryResponseDto.class,
					chatRoom.chatRoomId,
					chatRoom.chatRoomName,
					chatRoom.receiver.username,
					chatRoom.sender.username
				))
			.from(chatRoom)
			.where(searchCondition(chatRoomName, receiverName, userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
		boolean hasNext = roomList.size() > pageable.getPageSize();
		if (hasNext) {
			roomList.remove(roomList.size() - 1);
		}
		return new SliceImpl<>(roomList, pageable, hasNext);
	}

	private BooleanExpression searchCondition(String chatRoomName, String receiverName, Long userId) {
		BooleanExpression isNotDeleted = chatRoom.deletedBy.isNull();
		BooleanExpression isUser = chatRoom.sender.userId.eq(userId).or(chatRoom.receiver.userId.eq(userId));

		BooleanExpression isResult=isUser.and(isNotDeleted);
		if (chatRoomName == null && receiverName == null) {
			return isResult;
		} else {
			if (chatRoomName != null) {
				BooleanExpression isChatRoomName = chatRoom.chatRoomName.eq(chatRoomName);
				isResult=isResult.and(isChatRoomName);
			}
			if (receiverName != null) {
				BooleanExpression isReceiverSender = chatRoom.sender.username.eq(receiverName).or(chatRoom.receiver.username.eq(receiverName));
				isResult = isResult.and(isReceiverSender);
			}
		}
		return isResult;
	}
}
