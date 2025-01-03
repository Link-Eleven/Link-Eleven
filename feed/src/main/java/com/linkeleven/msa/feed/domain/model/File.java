package com.linkeleven.msa.feed.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_file")
public class File {

	@Id
	@Tsid
	private Long fileId;

	@Column(name = "s3_url")
	private String s3Url;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_size")
	private Long fileSize;

	public static File of(String s3Url, String fileName, Long fileSize) {
		return File.builder()
			.s3Url(s3Url)
			.fileName(fileName)
			.fileSize(fileSize)
			.build();
	}
}