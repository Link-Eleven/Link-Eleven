package com.linkeleven.msa.interaction.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.application.dto.ReplyCreateResponseDto;
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
		checkExistsComment(commentId);
		ContentDetails contentDetails = ContentDetails.of(requestDto.getContent(), userId);
		Reply reply = Reply.of(contentDetails, commentId);
		replyRepository.save(reply);
		return ReplyCreateResponseDto.of(
			reply.getId(),
			reply.getCommentId(),
			reply.getContentDetails().getUserId(),
			reply.getContentDetails().getContent());
	}

	private void checkExistsComment(Long commentId) {
		if (!existsComment(commentId)) {
			throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
		}
	}

	private boolean existsComment(Long commentId) {
		return commentRepository.existsByIdAndDeletedAtIsNull(commentId);
	}

}
