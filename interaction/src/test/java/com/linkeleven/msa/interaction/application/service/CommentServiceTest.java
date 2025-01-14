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
import com.linkeleven.msa.interaction.application.dto.external.UserInfoResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.infrastructure.client.AuthClient;
import com.linkeleven.msa.interaction.infrastructure.client.FeedClient;
import com.linkeleven.msa.interaction.presentation.dto.CommentCreateRequestDto;
import com.linkeleven.msa.interaction.presentation.dto.CommentUpdateRequestDto;

@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

	@MockitoBean
	private FeedClient feedClient;

	@MockitoBean
	private AuthClient authClient;

	private Long userId;
	private Long feedId;
	private Long feedAuthorId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		feedId = 100L;
		feedAuthorId = 9L;

		Mockito.when(feedClient.checkFeedExists(100L, 9L)).thenReturn(true);

		UserInfoResponseDto userInfo = new UserInfoResponseDto("username");
		Mockito.when(authClient.getUsername(1L)).thenReturn(userInfo);

	}

	@Test
	@DisplayName("댓글 생성 성공")
	void createComment() {
		String content = "테스트";
		CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
		requestDto.setContent(content);
		requestDto.setAuthorId(feedAuthorId);

		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);

		assertThat(responseDto.getCommentId()).isNotNull();
		assertThat(responseDto.getContent()).isEqualTo(content);
		assertThat(responseDto.getUsername()).isEqualTo("username");
	}

	@Test
	@DisplayName("댓글 수정 성공")
	void updatedComment() {
		String content = "테스트";
		CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
		requestDto.setContent(content);
		requestDto.setAuthorId(feedAuthorId);
		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);
		Long commentId = responseDto.getCommentId();

		String newContent = "수정 테스트";
		CommentUpdateRequestDto newRequestDto = new CommentUpdateRequestDto();
		newRequestDto.setContent(newContent);
		commentService.updateComment(userId, feedId, commentId, newRequestDto);

		var updateComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new AssertionError("수정된 댓글이 없습니다."));
		assertThat(updateComment.getContentDetails().getContent()).isEqualTo(newContent);
	}

	@Test
	@DisplayName("댓글 삭제 성공")
	void deletedComment() {
		String content = "테스트";
		CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
		requestDto.setContent(content);
		requestDto.setAuthorId(feedAuthorId);
		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);
		Long commentId = responseDto.getCommentId();

		commentService.deleteComment(userId, feedId, commentId);

		var deleteComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new AssertionError("삭제됨.  존재하지 않습니다."));
		assertThat(deleteComment.getDeletedAt()).isNotNull();
		assertThat(deleteComment.getContentDetails().getContent())
			.isEqualTo("삭제된 댓글입니다.");
	}
}