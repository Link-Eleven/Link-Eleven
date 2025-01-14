package com.linkeleven.msa.interaction.presentation.dto;

import com.linkeleven.msa.interaction.presentation.validation.ValidateContent;

import lombok.Data;

@Data
public class CommentCreateRequestDto {

	@ValidateContent
	private String Content;

	private Long authorId;

}
