package com.linkeleven.msa.interaction.domain.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Slice;

import com.linkeleven.msa.interaction.application.dto.CommentQueryResponseDto;

public interface CommentRepositoryCustom {

	Slice<CommentQueryResponseDto> findCommentByFeedWithCursor(
		Long feedId, Long cursorId, int pageSize, String sortByEnum, Long cursorLikeCount, LocalDateTime cursorCreatedAt);

	Long countByFeedId(Long feedId);
}
