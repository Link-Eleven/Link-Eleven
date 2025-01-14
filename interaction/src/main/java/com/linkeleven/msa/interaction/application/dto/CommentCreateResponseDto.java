package com.linkeleven.msa.interaction.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateResponseDto {

	private Long commentId;
	private Long userId;
	private String username;
	private String content;

	public static CommentCreateResponseDto of(Long commentId, Long userId, String username, String content) {
		return CommentCreateResponseDto.builder()
			.commentId(commentId)
			.userId(userId)
			.username(username)
			.content(content)
			.build();
	}
}


