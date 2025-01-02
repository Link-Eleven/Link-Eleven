package com.linkeleven.msa.interaction.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.application.dto.ReplyQueryResponseDto;
import com.linkeleven.msa.interaction.domain.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyQueryService {

	private final ReplyRepository replyRepository;

	public List<ReplyQueryResponseDto> getRepliesWithCursor(
		Long commentId, Long cursorId, LocalDateTime cursorCreatedAt, Long cursorLikeCount,
		int pageSize, String sortByEnum)
	{
		return replyRepository.findReplyByCommentWithCursor(commentId, cursorId, pageSize, sortByEnum,
			cursorLikeCount, cursorCreatedAt);
	}
}
