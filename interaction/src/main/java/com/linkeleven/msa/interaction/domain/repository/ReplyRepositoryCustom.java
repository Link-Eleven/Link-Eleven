package com.linkeleven.msa.interaction.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.linkeleven.msa.interaction.application.dto.ReplyQueryResponseDto;

public interface ReplyRepositoryCustom {

	List<ReplyQueryResponseDto> findReplyByCommentWithCursor(
		Long commentId, Long cursorId, int pageSize, String sortByEnum, Long cursorLikeCount, LocalDateTime cursorCreatedAt);

}
