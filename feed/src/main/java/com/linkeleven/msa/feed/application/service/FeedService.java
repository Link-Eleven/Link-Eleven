package com.linkeleven.msa.feed.application.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.application.dto.external.PopularFeedResponseDto;
import com.linkeleven.msa.feed.application.dto.external.UserValidateIdResponseDto;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.model.TopFeed;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.domain.repository.TopFeedRepository;
import com.linkeleven.msa.feed.infrastructure.client.AuthClient;
import com.linkeleven.msa.feed.infrastructure.client.CouponClient;
import com.linkeleven.msa.feed.infrastructure.client.InteractionClient;
import com.linkeleven.msa.feed.infrastructure.messaging.UserActivityProducer;
import com.linkeleven.msa.feed.libs.exception.CustomException;
import com.linkeleven.msa.feed.libs.exception.ErrorCode;
import com.linkeleven.msa.feed.presentation.request.FeedCreateRequestDto;
import com.linkeleven.msa.feed.presentation.request.FeedUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;
	private final TopFeedRepository topFeedRepository;
	private final FileService fileService;
	private final InteractionClient interactionClient;
	private final AuthClient authClient;
	private final CouponClient couponClient;
	private final RedisTemplate<String, List<FeedTopResponseDto>> redisTemplate;
	private final RedisTemplate<String, Object> opsHashRedisTemplate;
	private final UserActivityProducer userActivityProducer;

	@Transactional
	public FeedCreateResponseDto createFeed(FeedCreateRequestDto feedCreateRequestDto, List<MultipartFile> files,
		Long userId) {

		UserValidateIdResponseDto userInfo = getValidateUserId(userId);
		validateUser(userId, userInfo);

		Feed feed = Feed.of(
			userId,
			feedCreateRequestDto.getLocationId(),
			feedCreateRequestDto.getTitle(),
			feedCreateRequestDto.getContent(),
			feedCreateRequestDto.getCategory(),
			feedCreateRequestDto.getRegionEnum()
		);

		feed.getFiles().addAll(fileService.uploadFiles(files));

		Feed savedFeed = feedRepository.save(feed);

		return FeedCreateResponseDto.from(savedFeed);
	}

	@Transactional
	public FeedUpdateResponseDto updateFeed(Long feedId, FeedUpdateRequestDto feedUpdateRequestDto,
		List<MultipartFile> files, Long userId, String userRole) {

		Feed feed = findByIdAndDeletedAt(feedId);

		confirmFeedPermission(feed, userId, userRole);

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

		Feed feed = findByIdAndDeletedAt(feedId);

		confirmFeedPermission(feed, userId, userRole);

		feed.delete(userId);

		fileService.deleteFiles(feed, userId);

		couponClient.deleteCoupon(feedId, userId, userRole);

	}

	@Transactional
	public FeedReadResponseDto getDetailsByFeedId(Long feedId) {
		confirmFeedExists(feedId);

		feedRepository.incrementViews(feedId);

		Feed feed = findByIdAndDeletedAt(feedId);

		FeedReadResponseDto responseDto = FeedReadResponseDto.from(feed);

		userActivityProducer.sendActivity(feed.getUserId(), feed.getTitle());

		return responseDto;
	}

	@Transactional
	public void updateTopFeed() {
		opsHashRedisTemplate.delete("commentCounts");
		opsHashRedisTemplate.delete("likeCounts");

		LocalDateTime adjustedCurrentDate = LocalDateTime.now().withHour(23).withMinute(30).withSecond(0).withNano(0);
		LocalDateTime cutoffDate = adjustedCurrentDate.minusDays(7);

		List<Feed> feeds = feedRepository.findTopFeeds(cutoffDate);
		List<Long> feedIdList = feeds.stream().map(Feed::getFeedId).toList();
		Map<Long, Integer> commentCounts = interactionClient.getCommentCount(feedIdList).getCount();
		Map<Long, Integer> likeCounts = interactionClient.getLikeCount(feedIdList).getCount();

		feeds.forEach(feed -> {
			long daysSincePosted = ChronoUnit.DAYS.between(feed.getCreatedAt(), adjustedCurrentDate);
			int weight = (daysSincePosted <= 7) ? (8 - (int)daysSincePosted) : 1;
			int commentCount = commentCounts.getOrDefault(feed.getFeedId(), 0);
			int likeCount = likeCounts.getOrDefault(feed.getFeedId(), 0);
			double popularityScore = calculatePopularityScore(feed.getViews(), commentCount, likeCount, weight);
			feed.updatePopularityScore(popularityScore);
		});

		List<FeedTopResponseDto> topFeedList = feeds.stream()
			.sorted(Comparator.comparingDouble(Feed::getPopularityScore).reversed())
			.limit(100)
			.map(feed -> FeedTopResponseDto.of(feed,
				commentCounts.getOrDefault(feed.getFeedId(), 0),
				likeCounts.getOrDefault(feed.getFeedId(), 0)))
			.toList();

		backupTopFeed(topFeedList);

		redisTemplate.opsForValue().set("popularFeeds", topFeedList);
	}

	@Transactional
	public void backupTopFeed(List<FeedTopResponseDto> topFeedList) {
		List<TopFeed> backupList = topFeedList.stream()
			.map(TopFeed::of)
			.toList();

		topFeedRepository.saveAll(backupList);
	}

	private double calculatePopularityScore(int views, long commentCount, long likeCount, int weight) {
		double popularityScore = (views * 0.2 + commentCount * 0.5 + likeCount * 0.3) * weight;

		String formattedScore = String.format("%.2f", popularityScore);
		return Double.parseDouble(formattedScore);
	}

	public List<FeedTopResponseDto> getAllTopFeed() {
		return topFeedListFromCacheOrDb();
	}

	public List<FeedTopResponseDto> getTopFeed() {
		return topFeedListFromCacheOrDb().stream().limit(3).toList();
	}

	private List<FeedTopResponseDto> topFeedListFromCacheOrDb() {
		List<FeedTopResponseDto> top100FeedList = redisTemplate.opsForValue().get("popularFeeds");
		if (top100FeedList == null || top100FeedList.isEmpty()) {
			top100FeedList = topFeedRepository.findAll().stream().map(TopFeed::toDto).toList();
			redisTemplate.opsForValue().set("popularFeeds", top100FeedList);
		}
		return top100FeedList;
	}

	public List<PopularFeedResponseDto> getPopularFeedForCoupon() {
		return getTopFeed().stream()
			.map(feed -> new PopularFeedResponseDto(feed.getFeedId(), feed.getUserId()))
			.collect(Collectors.toList());
	}

	private Feed findByIdAndDeletedAt(Long feedId) {
		return feedRepository.findByIdAndDeletedAt(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));
	}

	private void confirmFeedExists(Long feedId) {
		if (!feedRepository.existsByFeedId(feedId)) {
			throw new CustomException(ErrorCode.FEED_NOT_FOUND);
		}
	}

	private void confirmFeedPermission(Feed feed, Long userId, String userRole) {
		if (!feed.getUserId().equals(userId) && !userRole.equals("MASTER")) {
			throw new CustomException(ErrorCode.NO_FEED_PERMISSION);
		}
	}

	private void validateUser(Long userId, UserValidateIdResponseDto userInfo) {
		if (userInfo == null || !userId.equals(userInfo.getUserId())) {
			throw new CustomException(ErrorCode.INVALID_USER);
		}
	}

	private UserValidateIdResponseDto getValidateUserId(Long userId) {
		return authClient.getValidateUserId(userId);
	}

	public boolean checkFeedExists(Long feedId, Long userId) {
		return feedRepository.existsByFeedIdAndUserId(feedId, userId);
	}

}