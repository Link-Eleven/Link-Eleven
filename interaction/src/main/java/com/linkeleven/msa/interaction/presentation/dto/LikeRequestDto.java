package com.linkeleven.msa.interaction.presentation.dto;

import lombok.Data;

@Data
public class LikeRequestDto {

	private Long targetAuthorId;

	// private String contentType;
	// public ContentType validateContentType() {
	// 	return ContentType.fromString(contentType);
	// }
}
