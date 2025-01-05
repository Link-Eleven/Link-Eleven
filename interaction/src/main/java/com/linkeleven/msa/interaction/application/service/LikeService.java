package com.linkeleven.msa.interaction.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.interaction.domain.model.entity.Like;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.domain.model.vo.Target;
import com.linkeleven.msa.interaction.domain.repository.LikeRepository;
import com.linkeleven.msa.interaction.domain.service.ValidationService;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;
import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

	private final LikeRepository likeRepository;
	private final ValidationService validationService;
	private final FeedClient feedClient;

	public void createLike(Long userId, Long targetId, ContentType contentType) {
		checkAlreadyLike(userId, targetId);
		validateTarget(targetId, contentType);

		Target target = Target.of(contentType, targetId);
		Like like = Like.of(target, userId);
		likeRepository.save(like);
	}

	public void cancelLike(Long userId, Long targetId, ContentType contentType) {
		Like like = getLike(userId, targetId);
		validateTarget(targetId, contentType);

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

	private void validateTarget(Long targetId, ContentType contentType) {
		boolean isValid = switch (contentType) {
			case FEED -> validateFeed(targetId);
			case COMMENT -> validateComment(targetId);
			case REPLY -> validateReply(targetId);
		};
		if (!isValid) {
			throw new CustomException(ErrorCode.TARGET_NOT_FOUND);
		}
	}

	private boolean validateReply(Long targetId) {
		return validationService.existsReply(targetId);
	}

	private boolean validateComment(Long targetId) {
		return validationService.existsComment(targetId);
	}

	private boolean validateFeed(Long targetId) {
		return feedClient.checkFeedExists(targetId);
	}

}
