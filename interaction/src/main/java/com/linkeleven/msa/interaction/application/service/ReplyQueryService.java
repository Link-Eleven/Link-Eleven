package com.linkeleven.msa.interaction.application.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.interaction.application.dto.ReplyQueryResponseDto;
import com.linkeleven.msa.interaction.domain.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyQueryService {

	private final ReplyRepository replyRepository;

	public Slice<ReplyQueryResponseDto> getRepliesWithCursor(
		Long commentId, Long cursorId, LocalDateTime cursorCreatedAt, Long cursorLikeCount,
		int pageSize, String sortByEnum)
	{
		return replyRepository.findReplyByCommentWithCursor(commentId, cursorId, pageSize, sortByEnum,
			cursorLikeCount, cursorCreatedAt);
	}
}
