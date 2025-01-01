package com.linkeleven.msa.interaction.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyCreateResponseDto {

	private Long replyId;
	private Long commentId;
	private Long userId;
	private String username;
	private String content;

	public static ReplyCreateResponseDto of(Long replyId, Long commentId, Long userId, String username, String content) {
		return ReplyCreateResponseDto.builder()
			.replyId(replyId)
			.commentId(commentId)
			.userId(userId)
			.username(username)
			.content(content)
			.build();
	}
}
