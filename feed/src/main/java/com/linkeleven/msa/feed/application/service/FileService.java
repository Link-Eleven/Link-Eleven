package com.linkeleven.msa.feed.application.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.model.File;
import com.linkeleven.msa.feed.domain.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final AmazonS3 amazonS3;
	private final FileRepository fileRepository;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	public String uploadImage(MultipartFile file) throws IOException {
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());

		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
			.withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3.getUrl(bucketName, fileName).toString();
	}

	public void deleteImage(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		amazonS3.deleteObject(bucketName, fileName);
	}

	public List<File> uploadFiles(List<MultipartFile> files) {
		if (files == null || files.isEmpty()) {
			return Collections.emptyList();
		}
		return files.stream().map(file -> {
			try {
				String s3Url = uploadImage(file);
				return File.of(s3Url, file.getOriginalFilename(), file.getSize());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	public void deleteFiles(Feed feed) {
		List<File> files = feed.getFiles();
		files.forEach(file -> {
			deleteImage(file.getS3Url());
			fileRepository.delete(file);
		});
		feed.getFiles().clear();
	}
}
