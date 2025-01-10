package com.linkeleven.msa.interaction.domain.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.domain.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidationService {

	private final CommentRepository commentRepository;
	private final ReplyRepository replyRepository;

	public boolean existsComment(Long targetId, Long targetAuthorId) {
		return commentRepository.existsByIdAndContentDetailsUserId(targetId, targetAuthorId);
	}

	public boolean existsReply(Long targetId, Long targetAuthorId) {
		return replyRepository.existsByIdAndContentDetails_userId(targetId, targetAuthorId);
	}

	public boolean existsCommentNotDeleted(Long commentId) {
		return commentRepository.existsByIdAndDeletedAtIsNull(commentId);
	}
}
