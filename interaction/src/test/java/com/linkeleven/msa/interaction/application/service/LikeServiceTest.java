package com.linkeleven.msa.interaction.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.domain.repository.LikeRepository;
import com.linkeleven.msa.interaction.domain.service.ValidationService;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;

@SpringBootTest
@ActiveProfiles("test")
class LikeServiceTest {

	@Autowired
	private LikeService likeService;

	@Autowired
	private LikeRepository likeRepository;

	@MockitoBean
	private ValidationService validationService;

	@MockitoBean
	private FeedClient feedClient;

	private Long userId;
	private Long targetFeedId;
	private Long targetCommentId;
	private Long targetReplyId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		targetFeedId = 100L;
		targetCommentId = 200L;
		targetReplyId = 300L;

		when(feedClient.checkFeedExists(100L)).thenReturn(true);
		when(validationService.existsComment(targetCommentId)).thenReturn(true);
		when(validationService.existsReply(targetReplyId)).thenReturn(true);
	}

	@AfterEach
	void clean() {
		likeRepository.deleteAll();
	}
	@Nested
	@DisplayName("게시글 테스트")
	class FeedLikeTests{
		@Test
		@Order(1)
		@DisplayName("게시글 좋아요 생성")
		void createLike_feed() {
			ContentType contentType = ContentType.FEED;
			likeService.createLike(userId, targetFeedId, contentType);

			var like = likeRepository.findByTarget_TargetIdAndUserId(targetFeedId, userId)
				.orElseThrow(() -> new AssertionError("못찾음"));
			assertThat(like.getTarget().getTargetId()).isEqualTo(targetFeedId);
			assertThat(like.getTarget().getContentType()).isEqualTo(contentType);
			assertThat(like.getUserId()).isEqualTo(userId);

			verify(feedClient, times(1)).checkFeedExists(targetFeedId);
		}

		@Test
		@Order(2)
		@DisplayName("게시글 좋아요 취소")
		void cancelLike_feed() {
			ContentType contentType = ContentType.FEED;
			likeService.createLike(userId, targetFeedId, contentType);

			likeService.cancelLike(userId, targetFeedId, contentType);

			boolean existsLike = likeRepository.findByTarget_TargetIdAndUserId(targetFeedId, userId).isPresent();
			assertThat(existsLike).isFalse();
		}
	}

	@Nested
	@DisplayName("댓글 테스트")
	class CommentLikeTests {
		@Test
		@Order(3)
		@DisplayName("댓글 좋아요 생성")
		void createLike_comment() {
			ContentType contentType = ContentType.COMMENT;
			likeService.createLike(userId, targetCommentId, contentType);

			var like = likeRepository.findByTarget_TargetIdAndUserId(targetCommentId, userId)
				.orElseThrow(() -> new AssertionError("못찾음"));
			assertThat(like.getTarget().getTargetId()).isEqualTo(targetCommentId);
			assertThat(like.getTarget().getContentType()).isEqualTo(contentType);
			assertThat(like.getUserId()).isEqualTo(userId);
		}

		@Test
		@Order(4)
		@DisplayName("댓글 좋아요 취소")
		void cancelLike_comment() {
			ContentType contentType = ContentType.COMMENT;
			likeService.createLike(userId, targetCommentId, contentType);

			likeService.cancelLike(userId, targetCommentId, contentType);

			boolean existsLike = likeRepository.findByTarget_TargetIdAndUserId(targetCommentId, userId).isPresent();
			assertThat(existsLike).isFalse();
		}
	}

	@Nested
	@DisplayName("대댓글 테스트")
	class ReplyLikeTests {
		@Test
		@Order(5)
		@DisplayName("대댓글 좋아요 생성")
		void createLike_reply() {
			ContentType contentType = ContentType.REPLY;
			likeService.createLike(userId, targetReplyId, contentType);

			var like = likeRepository.findByTarget_TargetIdAndUserId(targetReplyId, userId)
				.orElseThrow(() -> new AssertionError("못찾음"));
			assertThat(like.getTarget().getTargetId()).isEqualTo(targetReplyId);
			assertThat(like.getTarget().getContentType()).isEqualTo(contentType);
			assertThat(like.getUserId()).isEqualTo(userId);
		}

		@Test
		@Order(6)
		@DisplayName("대댓글 좋아요 취소")
		void cancelLike_reply() {
			ContentType contentType = ContentType.REPLY;
			likeService.createLike(userId, targetReplyId, contentType);

			likeService.cancelLike(userId, targetReplyId, contentType);

			boolean existsLike = likeRepository.findByTarget_TargetIdAndUserId(targetReplyId, userId).isPresent();
			assertThat(existsLike).isFalse();
		}
	}
}