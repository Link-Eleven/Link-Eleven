package com.linkeleven.msa.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkeleven.msa.auth.domain.model.ChatRoom;
import com.linkeleven.msa.auth.domain.model.User;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
	boolean existsBySenderAndReceiver(User sender, User receiver);
}
