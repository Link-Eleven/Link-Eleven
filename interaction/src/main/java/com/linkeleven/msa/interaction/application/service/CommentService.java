package com.linkeleven.msa.interaction.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.application.dto.CommentCreateResponseDto;
import com.linkeleven.msa.interaction.domain.model.entity.Comment;
import com.linkeleven.msa.interaction.domain.model.vo.ContentDetails;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;
import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;
import com.linkeleven.msa.interaction.presentation.dto.CommentCreateRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

	private final CommentRepository commentRepository;
	private final FeedClient feedClient;

	public CommentCreateResponseDto createComment(Long userId, Long feedId, CommentCreateRequestDto requestDto) {
		// checkFeedExists(feedId);
		ContentDetails contentDetails = ContentDetails.of(requestDto.getContent(), userId);
		Comment comment = Comment.of(contentDetails, feedId);
		commentRepository.save(comment);
		return CommentCreateResponseDto.of(
			comment.getId(),
			comment.getContentDetails().getUserId(),
			comment.getContentDetails().getContent());
	}

	private void checkFeedExists(Long feedId) {
		if (!feedClient.checkFeedExists(feedId)) {
			throw new CustomException(ErrorCode.FEED_NOT_FOUND);
		}
	}
}
