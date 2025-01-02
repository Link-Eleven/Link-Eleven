package com.linkeleven.msa.interaction.presentation.dto;

import com.linkeleven.msa.interaction.presentation.validation.ValidateContent;

import lombok.Data;

@Data
public class ReplyUpdateRequestDto {

	@ValidateContent
	private String Content;

}
