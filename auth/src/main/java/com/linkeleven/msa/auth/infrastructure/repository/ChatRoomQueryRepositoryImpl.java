package com.linkeleven.msa.auth.infrastructure.repository;

import static com.linkeleven.msa.auth.domain.model.QChatRoom.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.domain.model.ChatRoom;
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
}
