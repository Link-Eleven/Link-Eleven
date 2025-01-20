package com.linkeleven.msa.auth.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_chat")
public class Chat extends BaseTime {
	@Id
	@Tsid
	private Long chatId;

	@Column(name = "message", nullable = false)
	private String message;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_user", nullable = false)
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="chatRoom",nullable = false)
	private ChatRoom chatRoom;

	public static Chat createChat(String message, User sender, ChatRoom chatRoom) {
		return Chat.builder()
			.message(message)
			.sender(sender)
			.chatRoom(chatRoom)
			.build();
	}

}
