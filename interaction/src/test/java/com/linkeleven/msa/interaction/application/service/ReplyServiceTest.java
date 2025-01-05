package com.linkeleven.msa.interaction.application.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.linkeleven.msa.interaction.application.dto.CommentCreateResponseDto;
import com.linkeleven.msa.interaction.application.dto.ReplyCreateResponseDto;
import com.linkeleven.msa.interaction.application.dto.external.UserInfoResponseDto;
import com.linkeleven.msa.interaction.domain.repository.ReplyRepository;
import com.linkeleven.msa.interaction.domain.service.ValidationService;
import com.linkeleven.msa.interaction.infrastructure.client.AuthClient;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;
import com.linkeleven.msa.interaction.presentation.dto.CommentCreateRequestDto;
import com.linkeleven.msa.interaction.presentation.dto.ReplyCreateRequestDto;
import com.linkeleven.msa.interaction.presentation.dto.ReplyUpdateRequestDto;

@SpringBootTest
@ActiveProfiles("test")
class ReplyServiceTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ReplyRepository replyRepository;

	@MockitoBean
	private FeedClient feedClient;

	@MockitoBean
	private AuthClient authClient;

	private Long userId;
	private Long commentId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		Long feedId = 50L;

		Mockito.when(feedClient.checkFeedExists(50L)).thenReturn(true);

		UserInfoResponseDto userInfo = new UserInfoResponseDto("username");
		Mockito.when(authClient.getUsername(1L)).thenReturn(userInfo);

		String content = "테스트";
		CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
		requestDto.setContent(content);

		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);
		commentId = responseDto.getCommentId();
	}
	@Test
	@DisplayName("대댓글 생성 성공")
	void createReply() {
		String content = "테스트";
		ReplyCreateRequestDto requestDto = new ReplyCreateRequestDto();
		requestDto.setContent(content);

		ReplyCreateResponseDto responseDto = replyService.createReply(userId, commentId, requestDto);

		assertThat(responseDto.getReplyId()).isNotNull();
		assertThat(responseDto.getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("대댓글 수정 성공")
	void updateReply() {
		String content = "테스트";
		ReplyCreateRequestDto requestDto = new ReplyCreateRequestDto();
		requestDto.setContent(content);
		ReplyCreateResponseDto responseDto = replyService.createReply(userId, commentId, requestDto);
		Long replyId = responseDto.getReplyId();

		String newContent = "수정 테스트";
		ReplyUpdateRequestDto newRequestDto = new ReplyUpdateRequestDto();
		newRequestDto.setContent(newContent);
		replyService.updateReply(userId, replyId, commentId, newRequestDto);

		var updateReply = replyRepository.findById(replyId)
			.orElseThrow(() -> new AssertionError("수정된 댓글이 없습니다."));
		assertThat(updateReply.getContentDetails().getContent()).isEqualTo(newContent);
	}

	@Test
	@DisplayName("대댓글 삭제 성공")
	void deleteReply() {
		String content = "테스트";
		ReplyCreateRequestDto requestDto = new ReplyCreateRequestDto();
		requestDto.setContent(content);
		ReplyCreateResponseDto responseDto = replyService.createReply(userId, commentId, requestDto);
		Long replyId = responseDto.getReplyId();

		replyService.deleteReply(userId, commentId, replyId);

		var deleteReply = replyRepository.findById(replyId)
			.orElseThrow(() -> new AssertionError("삭제됨. 존재하지 않습니다."));
		assertThat(deleteReply.getDeletedAt()).isNotNull();
		assertThat(deleteReply.getId()).isEqualTo(replyId);
		assertThat(deleteReply.getContentDetails().getUserId()).isEqualTo(userId);
	}
}