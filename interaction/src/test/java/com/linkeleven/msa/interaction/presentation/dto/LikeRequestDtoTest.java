// package com.linkeleven.msa.interaction.presentation.dto;
//
// import static org.assertj.core.api.Assertions.*;
//
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Disabled;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
//
// import com.linkeleven.msa.interaction.domain.model.enums.ContentType;
// import com.linkeleven.msa.interaction.libs.exception.CustomException;
// import com.linkeleven.msa.interaction.libs.exception.ErrorCode;
//
// @Disabled
// class LikeRequestDtoTest {
//
// 	@Test
// 	@DisplayName("유효한 contentType인지 검증")
// 	@Disabled
// 	void validateContentType() {
// 		LikeRequestDto requestDto = new LikeRequestDto();
// 		requestDto.setContentType("FEED");
//
// 		ContentType type = requestDto.validateContentType();
//
// 		assertThat(type).isEqualTo(ContentType.FEED);
// 	}
//
// 	@Test
// 	@DisplayName("유효한 contentType인지 검증. 소문자 버전")
// 	@Disabled
// 	void validateContentType_LowerCase() {
// 		LikeRequestDto requestDto = new LikeRequestDto();
// 		requestDto.setContentType("rEpLy");
//
// 		ContentType type = requestDto.validateContentType();
//
// 		assertThat(type).isEqualTo(ContentType.REPLY);
// 	}
//
// 	@Test
// 	@DisplayName("실패 테스트 - 잘못된 값")
// 	@Disabled
// 	void failToValidateContentType() {
// 		LikeRequestDto requestDto = new LikeRequestDto();
// 		requestDto.setContentType("FE1212ED");
//
// 		CustomException exception = Assertions.assertThrows(CustomException.class, requestDto::validateContentType);
//
// 		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_CONTENT_TYPE);
// 		assertThat(exception.getMessage()).isEqualTo("유효하지않은 컨텐츠 타입입니다.");
// 	}
//
// 	@Test
// 	@DisplayName("실패 테스트 - null")
// 	@Disabled
// 	void failToValidateContentType_null() {
// 		LikeRequestDto requestDto = new LikeRequestDto();
// 		requestDto.setContentType(null);
//
// 		CustomException exception = Assertions.assertThrows(CustomException.class, requestDto::validateContentType);
//
// 		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONTENT_TYPE_CANNOT_BE_NULL);
// 		assertThat(exception.getMessage()).isEqualTo("컨텐츠 타입은 null일 수 없습니다.");
// 	}
//
// 	@Test
// 	@DisplayName("실패 테스트 - 빈 문자열")
// 	@Disabled
// 	void failToValidateContentType_empty() {
// 		LikeRequestDto requestDto = new LikeRequestDto();
// 		requestDto.setContentType("");
//
// 		CustomException exception = Assertions.assertThrows(CustomException.class, requestDto::validateContentType);
//
// 		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONTENT_TYPE_CANNOT_BE_NULL);
// 		assertThat(exception.getMessage()).isEqualTo("컨텐츠 타입은 null일 수 없습니다.");
// 	}
// }