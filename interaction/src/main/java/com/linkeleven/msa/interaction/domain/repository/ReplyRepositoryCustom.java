package com.linkeleven.msa.interaction.domain.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Slice;

import com.linkeleven.msa.interaction.application.dto.ReplyQueryResponseDto;

public interface ReplyRepositoryCustom {

	Slice<ReplyQueryResponseDto> findReplyByCommentWithCursor(
		Long commentId, Long cursorId, int pageSize, String sortByEnum, Long cursorLikeCount, LocalDateTime cursorCreatedAt);

}
