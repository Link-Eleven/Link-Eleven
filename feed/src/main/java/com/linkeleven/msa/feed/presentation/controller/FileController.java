// package com.linkeleven.msa.feed.presentation.controller;
//
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
//
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;
//
// import com.linkeleven.msa.feed.application.service.FileService;
//
// import lombok.RequiredArgsConstructor;

// 이미지 s3 저장 및 연동 -> 정상 작동하는지 테스트를 위한 컨트롤러 -> 게시글에 이미지 기능 반영에 의해 불필요(제거 예정)
// @RestController
// @RequestMapping("/api/files")
// @RequiredArgsConstructor
// public class FileController {
//
// 	private final FileService fileService;
//
// 	@PostMapping("/upload")
// 	public List<String> uploadImage(@RequestParam("file") List<MultipartFile> files) throws IOException {
// 		List<String> urls = new ArrayList<>();
// 		for (MultipartFile file : files) {
// 			urls.add(fileService.uploadImage(file));
// 		}
// 		return urls;
// 	}
//
// 	@DeleteMapping("/delete")
// 	public void deleteImage(@RequestParam("fileUrl") String fileUrl) {
// 		fileService.deleteImage(fileUrl);
// 	}
// }
