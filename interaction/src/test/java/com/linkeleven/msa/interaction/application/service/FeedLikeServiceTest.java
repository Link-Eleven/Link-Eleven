package com.linkeleven.msa.interaction.application.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.linkeleven.msa.interaction.domain.model.entity.Like;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.domain.model.vo.Target;
import com.linkeleven.msa.interaction.domain.repository.LikeRepository;
import com.linkeleven.msa.interaction.domain.service.ValidationService;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class FeedLikeServiceTest {

	@InjectMocks
	private LikeService likeService;

	@Mock
	private LikeRepository likeRepository;

	@Mock
	private ValidationService validationService;

	@Mock
	private FeedClient feedClient;

	private Long userId;
	private Long targetId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		targetId = 100L;

		when(feedClient.checkFeedExists(targetId)).thenReturn(true);
	}

	@Test
	@DisplayName("게시글 좋아요 생성")
	void createLike_feed() {
		ContentType contentType = ContentType.FEED;

		likeService.createLike(userId, targetId, contentType);

		verify(feedClient, times(1)).checkFeedExists(targetId);
		verify(likeRepository, times(1)).save(any(Like.class));
	}

	@Test
	@DisplayName("게시글 좋아요 취소")
	void cancelLike() {
		ContentType contentType = ContentType.FEED;

		Target target = Target.of(contentType, targetId);
		Like like = Like.of(target, userId);

		Mockito.when(likeRepository.findByTarget_TargetIdAndUserId(targetId, userId)).thenReturn(Optional.of(like));

		likeService.cancelLike(userId, targetId, contentType);

		verify(feedClient, times(1)).checkFeedExists(targetId);
		verify(likeRepository, times(1)).delete(like);
	}
}