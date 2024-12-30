package com.linkeleven.msa.interaction.application.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.linkeleven.msa.interaction.application.dto.CommentCreateResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.presentation.dto.CommentRequestDto;

@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

	private Long userId;
	private Long feedId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		feedId = 100L;
	}

	@Test
	@Order(1)
	@DisplayName("댓글 생성 성공")
	void createComment() {
		String content = "테스트";
		CommentRequestDto requestDto = new CommentRequestDto();
		requestDto.setContent(content);

		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);

		assertThat(responseDto.getCommentId()).isNotNull();
		assertThat(responseDto.getContent()).isEqualTo(content);
	}

	@Test
	@Order(2)
	@DisplayName("댓글 수정 성공")
	void updatedComment() {
		String content = "테스트";
		CommentRequestDto requestDto = new CommentRequestDto();
		requestDto.setContent(content);
		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);
		Long commentId = responseDto.getCommentId();

		String newContent = "수정 테스트";
		CommentRequestDto newRequestDto = new CommentRequestDto();
		newRequestDto.setContent(newContent);
		commentService.updateComment(userId, feedId, commentId, newRequestDto);

		var updateComment = commentRepository.findById(commentId)
			.orElseThrow(() -> new AssertionError("수정된 댓글이 없습니다."));
		assertThat(updateComment.getContentDetails().getContent()).isEqualTo(newContent);
	}
}