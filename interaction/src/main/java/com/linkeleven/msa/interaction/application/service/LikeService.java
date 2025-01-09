package com.linkeleven.msa.interaction.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.application.service.messaging.OutboxService;
import com.linkeleven.msa.interaction.domain.model.entity.Like;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.domain.model.vo.Target;
import com.linkeleven.msa.interaction.domain.repository.LikeRepository;
import com.linkeleven.msa.interaction.domain.service.ValidationService;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;
import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;
import com.linkeleven.msa.interaction.presentation.dto.LikeRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

	private final LikeRepository likeRepository;
	private final ValidationService validationService;
	private final OutboxService outboxService;
	private final FeedClient feedClient;

	public void createLike(Long userId, Long targetId, ContentType contentType, LikeRequestDto requestDto) {
		checkLogIn(userId);
		checkAlreadyLike(userId, targetId);
		validateTarget(targetId, requestDto.getTargetAuthorId(), contentType);

		Target target = Target.of(contentType, targetId);
		Like like = Like.of(target, userId);
		likeRepository.save(like);

		String lowerCaseContentType = contentType.name().toLowerCase();

		outboxService.saveLikeCreatedEvent(targetId, requestDto.getTargetAuthorId(),
			lowerCaseContentType, userId,
			like.getCreatedAt().toString(),"like_created");
	}

	public void cancelLike(Long userId, Long targetId, ContentType contentType, LikeRequestDto requestDto) {
		Like like = getLike(userId, targetId);
		validateTarget(targetId, requestDto.getTargetAuthorId(), contentType);

		likeRepository.delete(like);
	}

	private Like getLike(Long userId, Long targetId) {
		return likeRepository.findByTarget_TargetIdAndUserId(targetId, userId)
			.orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));
	}

	private void checkAlreadyLike(Long userId, Long targetId) {
		if (LikeAlreadyExists(userId, targetId)) {
			throw new CustomException(ErrorCode.ALREADY_LIKE);
		}
	}

	private boolean LikeAlreadyExists(Long userId, Long targetId) {
		return likeRepository.existsByTarget_TargetIdAndUserId(targetId, userId);
	}

	private void validateTarget(Long targetId, Long targetAuthorId, ContentType contentType) {
		boolean isValid = switch (contentType) {
			case FEED -> validateFeed(targetId, targetAuthorId);
			case COMMENT -> validateComment(targetId, targetAuthorId);
			case REPLY -> validateReply(targetId, targetAuthorId);
		};
		if (!isValid) {
			throw new CustomException(ErrorCode.TARGET_NOT_FOUND);
		}
	}

	private boolean validateReply(Long targetId, Long targetAuthorId) {
		return validationService.existsReply(targetId, targetAuthorId);
	}

	private boolean validateComment(Long targetId, Long targetAuthorId) {
		return validationService.existsComment(targetId, targetAuthorId);
	}

	private boolean validateFeed(Long targetId, Long targetAuthorId) {
		return feedClient.checkFeedExists(targetId, targetAuthorId);
	}


	private void checkLogIn(Long userId) {
		if (userId == null) {
			throw new CustomException(ErrorCode.INVALID_USERID);
		}
	}
}
