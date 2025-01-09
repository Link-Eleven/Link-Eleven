package com.linkeleven.msa.interaction.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.application.dto.CommentCreateResponseDto;
import com.linkeleven.msa.interaction.application.dto.CommentUpdateResponseDto;
import com.linkeleven.msa.interaction.application.dto.external.UserInfoResponseDto;
import com.linkeleven.msa.interaction.application.service.messaging.OutboxService;
import com.linkeleven.msa.interaction.domain.model.entity.Comment;
import com.linkeleven.msa.interaction.domain.model.vo.ContentDetails;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.infrastructure.client.AuthClient;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;
import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;
import com.linkeleven.msa.interaction.presentation.dto.CommentCreateRequestDto;
import com.linkeleven.msa.interaction.presentation.dto.CommentUpdateRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

	private final CommentRepository commentRepository;
	private final OutboxService outboxService;
	private final FeedClient feedClient;
	private final AuthClient authClient;

	public CommentCreateResponseDto createComment(Long userId, Long feedId, CommentCreateRequestDto requestDto) {
		checkLogIn(userId);
		UserInfoResponseDto userInfo = getUsername(userId);
		checkFeedExists(feedId, requestDto.getAuthorId());
		ContentDetails contentDetails = ContentDetails.of(requestDto.getContent(), userId, userInfo.getUsername());
		Comment comment = Comment.of(contentDetails, feedId);
		commentRepository.save(comment);

		outboxService.saveCommentCreatedEvent(
			feedId,
			requestDto.getAuthorId(),
			comment.getContentDetails().getContent(),
			comment.getContentDetails().getUserId(),
			comment.getContentDetails().getUsername(),
			comment.getCreatedAt().toString()
		);

		return CommentCreateResponseDto.of(
			comment.getId(),
			comment.getContentDetails().getUserId(),
			comment.getContentDetails().getUsername(),
			comment.getContentDetails().getContent());
	}

	public CommentUpdateResponseDto updateComment(Long userId, Long feedId, Long commentId, CommentUpdateRequestDto requestDto) {
		Comment comment = getComment(commentId);
		checkValidateComment(userId, feedId, comment);

		comment.updateComment(requestDto.getContent());

		return CommentUpdateResponseDto.of(
			comment.getId(),
			comment.getContentDetails().getUserId(),
			comment.getContentDetails().getUsername(),
			comment.getContentDetails().getContent());
	}

	public void deleteComment(Long userId, Long feedId, Long commentId) {
		Comment comment = getComment(commentId);
		checkValidateComment(userId, feedId, comment);

		comment.deleteComment();
	}

	private void checkValidateComment(Long userId, Long feedId, Comment comment) {
		checkUserId(userId, comment);
		checkValidFeed(feedId, comment);
		commentIsDeleted(comment);
	}

	private void checkUserId(Long userId, Comment comment) {
		if (!userId.equals(comment.getContentDetails().getUserId())) {
			throw new CustomException(ErrorCode.USER_IS_NOT_COMMENT_OWNER);
		}
	}

	private void checkValidFeed(Long feedId, Comment comment) {
		if (!feedId.equals(comment.getFeedId())) {
			throw new CustomException(ErrorCode.INVALID_FEED_FOR_COMMENT);
		}
	}

	private Comment getComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
	}

	private void commentIsDeleted(Comment comment) {
		if (isDeleted(comment.getDeletedAt())) {
			throw new CustomException(ErrorCode.COMMENT_ALREADY_DELETED);
		}
	}

	private boolean isDeleted(LocalDateTime localDateTime) {
		return localDateTime != null;
	}

	private void checkFeedExists(Long feedId, Long userId) {
		if (!feedClient.checkFeedExists(feedId, userId)) {
			throw new CustomException(ErrorCode.FEED_NOT_FOUND);
		}
	}

	private UserInfoResponseDto getUsername(Long userId) {
		return authClient.getUsername(userId);
	}

	private void checkLogIn(Long userId) {
		if (userId == null) {
			throw new CustomException(ErrorCode.INVALID_USERID);
		}
	}
}
