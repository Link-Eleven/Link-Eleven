package com.linkeleven.msa.interaction.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.application.dto.ReplyCreateResponseDto;
import com.linkeleven.msa.interaction.application.dto.ReplyUpdateResponseDto;
import com.linkeleven.msa.interaction.domain.model.entity.Reply;
import com.linkeleven.msa.interaction.domain.model.vo.ContentDetails;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.domain.repository.ReplyRepository;
import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;
import com.linkeleven.msa.interaction.presentation.dto.ReplyRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final CommentRepository commentRepository;

	public ReplyCreateResponseDto createReply(Long userId, Long commentId, ReplyRequestDto requestDto) {
		checkCommentExists(commentId);
		ContentDetails contentDetails = ContentDetails.of(requestDto.getContent(), userId);
		Reply reply = Reply.of(contentDetails, commentId);
		replyRepository.save(reply);
		return ReplyCreateResponseDto.of(
			reply.getId(),
			reply.getCommentId(),
			reply.getContentDetails().getUserId(),
			reply.getContentDetails().getContent());
	}

	public ReplyUpdateResponseDto updateReply(Long userId, Long replyId, Long commentId, ReplyRequestDto requestDto) {
		Reply reply = getReply(replyId);
		checkValidateReply(commentId, userId ,reply);
		reply.updateReply(requestDto.getContent());

		return ReplyUpdateResponseDto.of(
			reply.getId(),
			reply.getCommentId(),
			reply.getContentDetails().getUserId(),
			reply.getContentDetails().getContent());
	}

	public void deleteReply(Long userId, Long commentId, Long replyId) {
		Reply reply = getReply(replyId);
		checkValidateReply(commentId, userId, reply);
		reply.deleteReply();
	}

	private void checkValidateReply(Long commentId, Long userId, Reply reply) {
		checkCommentExists(commentId);
		checkUserId(userId, reply);
		checkValidComment(commentId, reply);
		replyIsDeleted(reply);
	}

	private void checkCommentExists(Long commentId) {
		if (!existsComment(commentId)) {
			throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
		}
	}

	private void checkValidComment(Long commentId, Reply reply) {
		if (!commentId.equals(reply.getCommentId())) {
			throw new CustomException(ErrorCode.INVALID_COMMENT_FOR_REPLY);
		}
	}

	private boolean existsComment(Long commentId) {
		return commentRepository.existsByIdAndDeletedAtIsNull(commentId);
	}

	private void checkUserId(Long userId, Reply reply) {
		if (!userId.equals(reply.getContentDetails().getUserId())) {
			throw new CustomException(ErrorCode.USER_IS_NOT_REPLY_OWNER);
		}
	}

	private void replyIsDeleted(Reply reply) {
		if (isDeleted(reply.getDeletedAt())) {
			throw new CustomException(ErrorCode.REPLY_ALREADY_DELETED);
		}
	}

	private boolean isDeleted(LocalDateTime localDateTime) {
		return localDateTime != null;
	}

	private Reply getReply(Long replyId) {
		return replyRepository.findById(replyId)
			.orElseThrow(() -> new CustomException(ErrorCode.REPLY_NOT_FOUND));
	}
}
