package com.linkeleven.msa.feed.application.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.model.File;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.domain.repository.FileRepository;
import com.linkeleven.msa.feed.libs.exception.CustomException;
import com.linkeleven.msa.feed.libs.exception.ErrorCode;
import com.linkeleven.msa.feed.presentation.request.FeedCreateRequestDto;
import com.linkeleven.msa.feed.presentation.request.FeedUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;
	private final FileService fileService;
	private final FileRepository fileRepository;

	@Transactional
	public FeedCreateResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto, List<MultipartFile> files) throws IOException {

		Feed feed = Feed.of(
			feedCreateRequestDto.getUserId(),
			feedCreateRequestDto.getLocationId(),
			feedCreateRequestDto.getTitle(),
			feedCreateRequestDto.getContent(),
			feedCreateRequestDto.getCategory()
		);

		if (files != null && !files.isEmpty()) {
			feed.getFiles().addAll(uploadFiles(files));
		}

		Feed savedFeed = feedRepository.save(feed);
		return FeedCreateResponseDto.from(savedFeed);
	}

	@Transactional
	public FeedUpdateResponseDto updateFeed(Long feedId, FeedUpdateRequestDto feedUpdateRequestDto, List<MultipartFile> files) throws IOException {
		Feed feed = feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		feed.update(
			feedUpdateRequestDto.getTitle(),
			feedUpdateRequestDto.getContent(),
			feedUpdateRequestDto.getCategory()
		);

		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				String s3Url = fileService.uploadImage(file);
				File fileEntity = File.of (
					s3Url,
					file.getOriginalFilename(),
					file.getSize()
				);
				feed.getFiles().add(fileEntity);
			}
		}

		Feed updatedFeed = feedRepository.save(feed);
		return FeedUpdateResponseDto.from(updatedFeed);
	}

	@Transactional
	public void deleteFeed(Long feedId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		for (File file : feed.getFiles()) {
			fileService.deleteImage(file.getS3Url());
			fileRepository.delete(file);
		}

		feed.delete();
		feedRepository.save(feed);
	}

	@Transactional
	public FeedReadResponseDto getDetailsByFeedId(Long feedId) {
		boolean exists = feedRepository.existsByFeedId(feedId);
		if (!exists) {
			throw new CustomException(ErrorCode.FEED_NOT_FOUND);
		}

		feedRepository.incrementViews(feedId);

		Feed feed = feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));
		return FeedReadResponseDto.from(feed);
	}

	public boolean checkFeedExists(Long feedId) {
		return feedRepository.existsById(feedId);
	}

	private List<File> uploadFiles(List<MultipartFile> files) throws IOException {
		return files.stream().map(file -> {
			String s3Url = null;
			try {
				s3Url = fileService.uploadImage(file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return File.of(s3Url, file.getOriginalFilename(), file.getSize());
		}).collect(Collectors.toList());
	}

	private void deleteFiles(List<File> files) {
		files.forEach(file -> {
			fileService.deleteImage(file.getS3Url());
			fileRepository.delete(file);
		});
	}
}
