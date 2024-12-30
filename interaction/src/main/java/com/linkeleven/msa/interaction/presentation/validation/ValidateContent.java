package com.linkeleven.msa.interaction.presentation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ContentValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateContent {

	String message() default "내용에 문제가 많습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
