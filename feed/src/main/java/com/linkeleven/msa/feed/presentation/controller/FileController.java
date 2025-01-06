package com.linkeleven.msa.feed.presentation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.linkeleven.msa.feed.application.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@PostMapping("/upload")
	public List<String> uploadImage(@RequestParam("file") List<MultipartFile> files) throws IOException {
		List<String> urls = new ArrayList<>();
		for (MultipartFile file : files) {
			urls.add(fileService.uploadImage(file));
		}
		return urls;
	}

	@DeleteMapping("/delete")
	public void deleteImage(@RequestParam("fileUrl") String fileUrl) {
		fileService.deleteImage(fileUrl);
	}
}