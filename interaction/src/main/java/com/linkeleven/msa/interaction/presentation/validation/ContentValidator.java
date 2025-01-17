package com.linkeleven.msa.interaction.presentation.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContentValidator implements ConstraintValidator<ValidateContent, String> {

	private static final String ALLOWED_PATTERN =
		"^[\\p{IsLatin}\\p{IsHangul}\\p{IsHan}\\p{IsHiragana}\\p{IsKatakana}\\p{IsCyrillic}\\s!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/♥★●\\u1F300-\\u1F6FF\\u1F900-\\u1F9FF\n\r]*$";

	@Override
	public boolean isValid(String content, ConstraintValidatorContext context) {
		if (content == null || content.isBlank()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("내용을 입력해주세요.")
				.addConstraintViolation();
			return false;
		}
		if (content.length() > 100) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("100자를 초과할 수 없습니다.")
				.addConstraintViolation();
			return false;
		}
		if (!Pattern.matches(ALLOWED_PATTERN, content)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("허용되지 않은 특수문자가 포함되어 있습니다.")
				.addConstraintViolation();
			return false;
		}
		return true;
	}
}
