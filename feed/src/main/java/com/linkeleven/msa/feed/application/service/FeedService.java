package com.linkeleven.msa.feed.application.service;

import java.io.IOException;
import java.util.Collections;
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
	public FeedCreateResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto, List<MultipartFile> files) throws
		IOException {

		Feed feed = Feed.of(
			feedCreateRequestDto.getUserId(),
			feedCreateRequestDto.getLocationId(),
			feedCreateRequestDto.getTitle(),
			feedCreateRequestDto.getContent(),
			feedCreateRequestDto.getCategory()
		);

		feed.getFiles().addAll(uploadFiles(files));

		Feed savedFeed = feedRepository.save(feed);

		return FeedCreateResponseDto.from(savedFeed);
	}

	@Transactional
	public FeedUpdateResponseDto updateFeed(Long feedId, FeedUpdateRequestDto feedUpdateRequestDto,
		List<MultipartFile> files) throws IOException {
		Feed feed = feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		feed.update(
			feedUpdateRequestDto.getTitle(),
			feedUpdateRequestDto.getContent(),
			feedUpdateRequestDto.getCategory()
		);

		feed.getFiles().addAll(uploadFiles(files));

		Feed updatedFeed = feedRepository.save(feed);

		return FeedUpdateResponseDto.from(updatedFeed);
	}

	@Transactional
	public void deleteFeed(Long feedId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		deleteFiles(feed);
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

	private List<File> uploadFiles(List<MultipartFile> files) {
		if (files == null || files.isEmpty()) {
			return Collections.emptyList();
		}
		return files.stream().map(file -> {
			try {
				String s3Url = fileService.uploadImage(file);
				return File.of(s3Url, file.getOriginalFilename(), file.getSize());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	private void deleteFiles(Feed feed) {
		List<File> files = feed.getFiles();
		files.forEach(file -> {
			fileService.deleteImage(file.getS3Url());
			fileRepository.delete(file);
		});
		feed.getFiles().clear();
	}
}
