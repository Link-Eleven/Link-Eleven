package com.linkeleven.msa.interaction.presentation.validation;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.linkeleven.msa.interaction.presentation.dto.CommentCreateRequestDto;

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
		CommentCreateRequestDto dto = new CommentCreateRequestDto();
		dto.setContent(null);

		Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("내용을 입력해주세요.");
	}

	@Test
	@DisplayName("dto의 content에 빈 값이 들어감")
	void blankTest() {
		CommentCreateRequestDto dto = new CommentCreateRequestDto();
		dto.setContent("  ");

		Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("내용을 입력해주세요.");
	}

	@Test
	@DisplayName("dto의 content 길이가 100이 넘어감")
	void lengthTest() {
		CommentCreateRequestDto dto = new CommentCreateRequestDto();
		dto.setContent("z".repeat(101));

		Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("100자를 초과할 수 없습니다.");
	}

	@Test
	@DisplayName("이상한 특수문자 포함")
	void invalidSpecialCharacterTest() {
		CommentCreateRequestDto dto = new CommentCreateRequestDto();
		dto.setContent("⎝ᑒ⎠");

		Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(dto);

		assertThat(violations)
			.isNotEmpty()
			.extracting(ConstraintViolation::getMessage)
			.containsOnly("허용되지 않은 특수문자가 포함되어 있습니다.");
	}

	@Test
	@DisplayName("멀쩡한 테스트")
	void successTest() {
		CommentCreateRequestDto dto = new CommentCreateRequestDto();
		dto.setContent("테스트 성공 success 😊 サクセス ひらがな 成功  éxito  succès  успех ");

		Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(dto);

		assertThat(violations).isNotEmpty();
	}
}