package com.linkeleven.msa.notification.domain.model.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
	COMMENT("{username}님이 댓글을 달았습니다."),
	REPLY("{username}님이 대댓글을 달았습니다."),
	LIKE("{username}님이 {contentType}에 좋아요를 눌렀습니다."),
	FOLLOW("{username}님이 팔로우하였습니다."),
	FEED("{username}님이 게시글을 작성하였습니다."),
	COUPON("쿠폰이 발행되었습니다.")

	;

	private final String messageTemplate;

	NotificationType(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public String generateMessage(String username, String contentType) {
		return messageTemplate
			.replace("{username}", username)
			.replace("{contentType}", contentType != null ? contentType : "");
	}
}
