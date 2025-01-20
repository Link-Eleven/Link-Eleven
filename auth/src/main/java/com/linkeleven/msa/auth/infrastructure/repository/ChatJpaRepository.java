package com.linkeleven.msa.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.auth.domain.model.Chat;
import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;

public interface ChatJpaRepository extends JpaRepository<Chat,Long> {

	boolean existsBySenderAndChatRoom(User sender, ChatRoom chatRoom);
}
