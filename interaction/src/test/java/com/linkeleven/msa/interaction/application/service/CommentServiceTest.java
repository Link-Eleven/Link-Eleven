package com.linkeleven.msa.interaction.application.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.linkeleven.msa.interaction.application.dto.CommentCreateResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.presentation.dto.CommentCreateRequestDto;

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
	@DisplayName("댓글 생성 성공")
	void createComment() {
		String content = "테스트";
		CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
		requestDto.setContent(content);

		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);

		assertThat(responseDto.getCommentId()).isNotNull();
		assertThat(responseDto.getContent()).isEqualTo(content);
	}
}