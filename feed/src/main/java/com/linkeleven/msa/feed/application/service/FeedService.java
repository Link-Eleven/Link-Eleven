package com.linkeleven.msa.feed.application.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.model.File;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.domain.repository.FileRepository;
import com.linkeleven.msa.feed.infrastructure.client.InteractionClient;
import com.linkeleven.msa.feed.infrastructure.config.TokenVerifier;
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
	private final TokenVerifier tokenVerifier;
	private final InteractionClient interactionClient;

	@Transactional
	public FeedCreateResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto, List<MultipartFile> files,
		String token) {

		Long userId = tokenVerifier.getUserIdFromToken(token);

		Feed feed = Feed.of(
			userId,
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
		List<MultipartFile> files, String token) {

		Long userId = tokenVerifier.getUserIdFromToken(token);
		String userRole = tokenVerifier.getRoleFromToken(token);

		Feed feed = feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		if (!feed.getUserId().equals(userId) && !userRole.equals("MASTER")) {
			throw new CustomException(ErrorCode.NO_UPDATE_PERMISSION);
		}

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
	public void deleteFeed(Long feedId, String token) {

		Long userId = tokenVerifier.getUserIdFromToken(token);
		String userRole = tokenVerifier.getRoleFromToken(token);

		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

		if (!feed.getUserId().equals(userId) && !userRole.equals("MASTER")) {
			throw new CustomException(ErrorCode.NO_DELETE_PERMISSION);
		}

		deleteFiles(feed);
		feed.delete(userId);
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

	@Transactional
	public List<FeedTopResponseDto> getTopFeed(int limit) {
		Pageable pageable = Pageable.unpaged();
		List<Feed> feeds = feedRepository.findTopFeeds(pageable);

		feeds.forEach(feed -> {
			long commentCount = interactionClient.getCommentCount(feed.getFeedId()).getCount();
			long likeCount = interactionClient.getLikeCount(feed.getFeedId()).getCount();
			double popularityScore = calculatePopularityScore(feed.getViews(), commentCount, likeCount);
			feed.updatePopularityScore(popularityScore);
			feedRepository.save(feed);
		});

		return feeds.stream()
			.sorted(Comparator.comparingDouble(Feed::getPopularityScore).reversed())
			.limit(limit)
			.map(feed -> {
				long commentCount = interactionClient.getCommentCount(feed.getFeedId()).getCount();
				long likeCount = interactionClient.getLikeCount(feed.getFeedId()).getCount();
				return FeedTopResponseDto.of(feed, commentCount, likeCount);
			})
			.collect(Collectors.toList());
	}


	private double calculatePopularityScore(int views, long commentCount, long likeCount) {
		double popularityScore = views * 0.2 + commentCount * 0.5 + likeCount * 0.3;

		String formattedScore = String.format("%.2f", popularityScore);
		return Double.parseDouble(formattedScore);
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
