package com.linkeleven.msa.interaction.application.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.linkeleven.msa.interaction.application.dto.CommentCreateResponseDto;
import com.linkeleven.msa.interaction.application.dto.ReplyCreateResponseDto;
import com.linkeleven.msa.interaction.domain.repository.CommentRepository;
import com.linkeleven.msa.interaction.domain.repository.ReplyRepository;
import com.linkeleven.msa.interaction.presentation.dto.CommentRequestDto;
import com.linkeleven.msa.interaction.presentation.dto.ReplyRequestDto;

@SpringBootTest
@ActiveProfiles("test")
class ReplyServiceTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private ReplyRepository replyRepository;

	private Long userId;
	private Long commentId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		Long feedId = 50L;

		String content = "테스트";
		CommentRequestDto requestDto = new CommentRequestDto();
		requestDto.setContent(content);

		CommentCreateResponseDto responseDto = commentService.createComment(userId, feedId, requestDto);
		commentId = responseDto.getCommentId();
	}
	@Test
	@DisplayName("대댓글 생성 성공")
	void createReply() {
		String content = "테스트";
		ReplyRequestDto requestDto = new ReplyRequestDto();
		requestDto.setContent(content);

		ReplyCreateResponseDto responseDto = replyService.createReply(userId, commentId, requestDto);

		assertThat(responseDto.getReplyId()).isNotNull();
		assertThat(responseDto.getContent()).isEqualTo(content);
	}
}