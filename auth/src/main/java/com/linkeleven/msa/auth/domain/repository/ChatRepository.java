package com.linkeleven.msa.auth.domain.repository;

import com.linkeleven.msa.auth.domain.model.Chat;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;

public interface ChatRepository {
	Chat save(Chat chat);

	boolean existsBySenderAndChatRoom(User sender, ChatRoom chatRoom);
}
