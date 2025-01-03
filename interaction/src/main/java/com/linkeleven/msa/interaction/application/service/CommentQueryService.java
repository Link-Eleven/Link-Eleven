package com.linkeleven.msa.interaction.application.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.interaction.application.dto.CommentQueryResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

	private final CommentRepository commentRepository;
	public Slice<CommentQueryResponseDto> getCommentsWithCursor(
		Long feedId, Long cursorId, LocalDateTime cursorCreatedAt, Long cursorLikeCount,
		int pageSize, String sortByEnum)
	{
		return commentRepository.findCommentByFeedWithCursor(feedId, cursorId, pageSize, sortByEnum,
			cursorLikeCount, cursorCreatedAt);
	}
}
