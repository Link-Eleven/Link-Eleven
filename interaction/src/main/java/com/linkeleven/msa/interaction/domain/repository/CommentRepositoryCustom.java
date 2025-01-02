package com.linkeleven.msa.interaction.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.linkeleven.msa.interaction.application.dto.CommentQueryResponseDto;

@NoRepositoryBean
public interface CommentRepositoryCustom {

	List<CommentQueryResponseDto> findCommentByFeedWithCursor(
		Long feedId, Long cursorId, int pageSize, String sortByEnum, Long cursorLikeCount, LocalDateTime cursorCreatedAt);

}
