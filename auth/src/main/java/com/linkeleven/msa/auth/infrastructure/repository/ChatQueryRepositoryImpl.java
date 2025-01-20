package com.linkeleven.msa.auth.infrastructure.repository;

import static com.linkeleven.msa.auth.domain.model.QChat.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.linkeleven.msa.auth.application.dto.ChatQueryResponseDto;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatQueryRepositoryImpl implements ChatQueryRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<ChatQueryResponseDto> findChatByChatRoom(ChatRoom chatRoom, Pageable pageable) {
		List<ChatQueryResponseDto> chatList = queryFactory
			.select(Projections.constructor(ChatQueryResponseDto.class,
				chat.sender.username,
				chat.message
			))
			.from(chat)
			.where(chat.chatRoom.eq(chatRoom))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
		boolean hasNext = chatList.size() > pageable.getPageSize();
		if (hasNext) {
			chatList.remove(chatList.size() - 1);
		}

		return new SliceImpl<>(chatList, pageable, hasNext);
	}

}

