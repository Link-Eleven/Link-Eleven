package com.linkeleven.msa.interaction.presentation.validation;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.linkeleven.msa.interaction.presentation.dto.CommentRequestDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
@SpringBootTest
@ActiveProfiles("test")
class ContentValidatorTest {

	@Autowired
	private Validator validator;
	@Test
	@DisplayName("dto의 content에 null값이 들어감")
	void nullTest() {
		CommentRequestDto dto = new CommentRequestDto();
		dto.setContent(null);

		Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("내용을 입력해주세요.");
	}

	@Test
	@DisplayName("dto의 content에 빈 값이 들어감")
	void blankTest() {
		CommentRequestDto dto = new CommentRequestDto();
		dto.setContent("  ");

		Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("내용을 입력해주세요.");
	}

	@Test
	@DisplayName("dto의 content 길이가 100이 넘어감")
	void lengthTest() {
		CommentRequestDto dto = new CommentRequestDto();
		dto.setContent("z".repeat(101));

		Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("100자를 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("이상한 특수문자 포함")
	void invalidSpecialCharacterTest() {
		CommentRequestDto dto = new CommentRequestDto();
		dto.setContent("⎝ᑒ⎠");

		Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("허용되지 않은 특수문자가 포함되어 있습니다.");
	}

	@Test
	@DisplayName("멀쩡한 테스트")
	void successTest() {
		CommentRequestDto dto = new CommentRequestDto();
		dto.setContent("테스트 성공 success 😊 サクセス ひらがな 成功  éxito  succès  успех ");

		Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(dto);

		assertThat(violations).isNotEmpty();
	}
}