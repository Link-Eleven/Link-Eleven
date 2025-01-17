package com.linkeleven.msa.interaction.application.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentQueryResponseDto {

	private Long commentId;
	private Long userId;
	private String username;
	private String content;
	private LocalDateTime createdAt;
	private Long likeCount;
	private Long replyCount;

}
