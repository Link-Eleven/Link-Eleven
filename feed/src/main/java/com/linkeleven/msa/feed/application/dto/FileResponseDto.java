package com.linkeleven.msa.feed.application.dto;

import com.linkeleven.msa.feed.domain.model.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponseDto {
	private String url;
	private String originalFilename;
	private long size;

	public static FileResponseDto from(File file) {
		return FileResponseDto.builder()
			.url(file.getS3Url())
			.originalFilename(file.getFileName())
			.size(file.getFileSize())
			.build();
	}
}
