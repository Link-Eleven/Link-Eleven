package com.linkeleven.msa.interaction.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateResponseDto {

	private Long commentId;
	private Long userId;
	private String username;
	private String content;

	public static CommentUpdateResponseDto of(Long commentId, Long userId, String username, String content) {
		return CommentUpdateResponseDto.builder()
			.commentId(commentId)
			.userId(userId)
			.username(username)
			.content(content)
			.build();
	}
}


