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
@Table(name = "p_chatroom")
public class ChatRoom extends BaseTime {
	@Id
	@Tsid
	private Long chatRoomId;

	@Column(name="chat_room_name",nullable=true)
	private String chatRoomName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sernder_user",nullable = false)
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_user",nullable = false)
	private User receiver;

	public static ChatRoom createChatRoom(String chatRoomName, User sender, User receiver) {
		return ChatRoom.builder()
			.chatRoomName(chatRoomName)
			.sender(sender)
			.receiver(receiver)
			.build();
	}
}
