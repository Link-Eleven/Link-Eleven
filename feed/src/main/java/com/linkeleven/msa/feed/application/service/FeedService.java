package com.linkeleven.msa.feed.application.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.application.dto.external.UserInfoResponseDto;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.infrastructure.client.AuthClient;
import com.linkeleven.msa.feed.infrastructure.client.InteractionClient;
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
	private final InteractionClient interactionClient;
	private final AuthClient authClient;

	@Transactional
	public FeedCreateResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto, List<MultipartFile> files,
		Long userId) {

		// 유저 정보 가져오기 및 검증
		// UserInfoResponseDto userInfo = authClient.getUsername(userId);
		// validateUser(userId, userInfo);

		Feed feed = Feed.of(
			userId,
			feedCreateRequestDto.getLocationId(),
			feedCreateRequestDto.getTitle(),
			feedCreateRequestDto.getContent(),
			feedCreateRequestDto.getCategory()
		);

		feed.getFiles().addAll(fileService.uploadFiles(files));

		Feed savedFeed = feedRepository.save(feed);

		return FeedCreateResponseDto.from(savedFeed);
	}

	@Transactional
	public FeedUpdateResponseDto updateFeed(Long feedId, FeedUpdateRequestDto feedUpdateRequestDto,
		List<MultipartFile> files, Long userId, String userRole) {

		// 유저 정보 가져오기 및 검증
		// UserInfoResponseDto userInfo = authClient.getUsername(userId);
		// validateUser(userId, userInfo);

		Feed feed = findByIdAndDeletedAt(feedId);

		if (!feed.getUserId().equals(userId) && !userRole.equals("MASTER")) {
			throw new CustomException(ErrorCode.NO_UPDATE_PERMISSION);
		}

		feed.update(
			feedUpdateRequestDto.getTitle(),
			feedUpdateRequestDto.getContent(),
			feedUpdateRequestDto.getCategory()
		);

		feed.getFiles().addAll(fileService.uploadFiles(files));

		Feed updatedFeed = feedRepository.save(feed);

		return FeedUpdateResponseDto.from(updatedFeed);
	}

	@Transactional
	public void deleteFeed(Long feedId, Long userId, String userRole) {

		// 유저 정보 가져오기 및 검증
		// UserInfoResponseDto userInfo = authClient.getUsername(userId);
		// validateUser(userId, userInfo);

		Feed feed = findByIdAndDeletedAt(feedId);

		if (!feed.getUserId().equals(userId) && !userRole.equals("MASTER")) {
			throw new CustomException(ErrorCode.NO_DELETE_PERMISSION);
		}

		feed.delete(userId);
		fileService.deleteFiles(feed, userId);

	}

	@Transactional
	public FeedReadResponseDto getDetailsByFeedId(Long feedId) {
		boolean exists = feedRepository.existsByFeedId(feedId);
		if (!exists) {
			throw new CustomException(ErrorCode.FEED_NOT_FOUND);
		}

		feedRepository.incrementViews(feedId);

		Feed feed = findByIdAndDeletedAt(feedId);
		return FeedReadResponseDto.from(feed);
	}

	@Transactional
	@Cacheable(value = "popularFeeds", key = "#limit", cacheManager = "cacheManager", unless = "#result == null || #result.isEmpty()")
	public List<FeedTopResponseDto> getTopFeed(int limit) {

		Pageable pageable = Pageable.unpaged();
		List<Feed> feeds = feedRepository.findTopFeeds(pageable)
			.stream()
			.filter(feed -> feed.getDeletedAt() == null)
			.toList();

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

	private Feed findByIdAndDeletedAt(Long feedId) {
		return feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));
	}

	private void validateUser(Long headerUserId, UserInfoResponseDto userInfo) {
		if (userInfo == null || userInfo.getUserId() == null || !headerUserId.equals(userInfo.getUserId())) {
			throw new CustomException(ErrorCode.INVALID_USER);
		}
	}

	public boolean checkFeedExists(Long feedId) {
		return feedRepository.existsById(feedId);
	}

}
