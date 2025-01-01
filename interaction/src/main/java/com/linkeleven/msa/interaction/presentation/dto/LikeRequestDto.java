package com.linkeleven.msa.interaction.presentation.dto;

import com.linkeleven.msa.interaction.domain.model.enums.ContentType;

import lombok.Data;

@Data
public class LikeRequestDto {

	private String contentType;

	public ContentType validateContentType() {
		return ContentType.fromString(contentType);
	}
}
