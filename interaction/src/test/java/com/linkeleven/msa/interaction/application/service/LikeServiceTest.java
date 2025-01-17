package com.linkeleven.msa.interaction.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.linkeleven.msa.interaction.application.dto.external.UserInfoResponseDto;
import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
import com.linkeleven.msa.interaction.domain.repository.LikeRepository;
import com.linkeleven.msa.interaction.domain.service.ValidationService;
import com.linkeleven.msa.interaction.infrastructure.client.AuthClient;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;
import com.linkeleven.msa.interaction.presentation.dto.LikeRequestDto;

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

	@MockitoBean
	private AuthClient authClient;

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
		Long targetAuthorId = 9L;

		when(feedClient.checkFeedExists(100L, 9L)).thenReturn(true);

		UserInfoResponseDto userInfo = new UserInfoResponseDto("username");
		Mockito.when(authClient.getUsername(1L)).thenReturn(userInfo);
		when(validationService.existsComment(targetCommentId, 9L)).thenReturn(true);
		when(validationService.existsReply(targetReplyId, 9L)).thenReturn(true);
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
			LikeRequestDto requestDto = new LikeRequestDto();
			requestDto.setTargetAuthorId(9L);
			ContentType contentType = ContentType.FEED;
			likeService.createLike(userId, targetFeedId, contentType, requestDto);

			var like = likeRepository.findByTarget_TargetIdAndUserId(targetFeedId, userId)
				.orElseThrow(() -> new AssertionError("못찾음"));
			assertThat(like.getTarget().getTargetId()).isEqualTo(targetFeedId);
			assertThat(like.getTarget().getContentType()).isEqualTo(contentType);
			assertThat(like.getUserId()).isEqualTo(userId);

			verify(feedClient, times(1)).checkFeedExists(targetFeedId, 9L);
		}

		@Test
		@Order(2)
		@DisplayName("게시글 좋아요 취소")
		void cancelLike_feed() {
			ContentType contentType = ContentType.FEED;
			LikeRequestDto requestDto = new LikeRequestDto();
			requestDto.setTargetAuthorId(9L);
			likeService.createLike(userId, targetFeedId, contentType, requestDto);

			likeService.cancelLike(userId, targetFeedId, contentType, requestDto);

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
			LikeRequestDto requestDto = new LikeRequestDto();
			requestDto.setTargetAuthorId(9L);
			likeService.createLike(userId, targetCommentId, contentType, requestDto);

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
			LikeRequestDto requestDto = new LikeRequestDto();
			requestDto.setTargetAuthorId(9L);
			likeService.createLike(userId, targetCommentId, contentType, requestDto);

			likeService.cancelLike(userId, targetCommentId, contentType, requestDto);

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
			LikeRequestDto requestDto = new LikeRequestDto();
			requestDto.setTargetAuthorId(9L);
			likeService.createLike(userId, targetReplyId, contentType, requestDto);

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
			LikeRequestDto requestDto = new LikeRequestDto();
			requestDto.setTargetAuthorId(9L);
			likeService.createLike(userId, targetReplyId, contentType, requestDto);

			likeService.cancelLike(userId, targetReplyId, contentType, requestDto);

			boolean existsLike = likeRepository.findByTarget_TargetIdAndUserId(targetReplyId, userId).isPresent();
			assertThat(existsLike).isFalse();
		}
	}
}