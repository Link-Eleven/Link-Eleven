package com.linkeleven.msa.feed.application.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.linkeleven.msa.feed.application.dto.FeedCreateResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedReadResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedSearchResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;
import com.linkeleven.msa.feed.application.dto.FeedUpdateResponseDto;
import com.linkeleven.msa.feed.application.dto.external.UserInfoResponseDto;
import com.linkeleven.msa.feed.domain.model.Category;
import com.linkeleven.msa.feed.domain.model.Feed;
import com.linkeleven.msa.feed.domain.repository.FeedRepository;
import com.linkeleven.msa.feed.infrastructure.client.AuthClient;
import com.linkeleven.msa.feed.infrastructure.client.CouponClient;
import com.linkeleven.msa.feed.infrastructure.client.InteractionClient;
import com.linkeleven.msa.feed.libs.exception.CustomException;
import com.linkeleven.msa.feed.libs.exception.ErrorCode;
import com.linkeleven.msa.feed.presentation.request.FeedCreateRequestDto;
import com.linkeleven.msa.feed.presentation.request.FeedSearchRequestDto;
import com.linkeleven.msa.feed.presentation.request.FeedUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;
	private final FileService fileService;
	private final InteractionClient interactionClient;
	private final AuthClient authClient;
	private final CouponClient couponClient;
	private final RedisTemplate<String, List<Feed>> redisTemplate;

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

		// 쿠폰 삭제 요청
		// couponClient.deleteCoupons(feedId);

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
	public void updateTopFeed() {

		// 현재 날짜 기준으로 23:30을 설정 (24시 기준으로 계산)
		LocalDateTime adjustedCurrentDate = LocalDateTime.now().withHour(23).withMinute(30).withSecond(0).withNano(0);

		// 일주일 전 날짜를 계산
		LocalDateTime cutoffDate = adjustedCurrentDate.minusDays(7);

		// 인기 게시글 조회
		List<Feed> feeds = feedRepository.findTopFeeds(cutoffDate);

		// feed Id 별 댓글 및 좋아요 수 조회
		List<Long> feedIds = feeds.stream().map(Feed::getFeedId).toList();
		Map<Long, Integer> commentCounts = interactionClient.getCommentCounts(
			feedIds); // map or dto(피드아이디 댓글 갯수) map 추천
		Map<Long, Integer> likeCounts = interactionClient.getLikeCounts(
			feedIds); // 1 long feedId 2 long size = integer (피드아이디 좋아요 수)

		// 각 피드의 인기도 점수 계산
		feeds.forEach(feed -> {
			long daysSincePosted = ChronoUnit.DAYS.between(feed.getCreatedAt(), adjustedCurrentDate);
			int weight = (daysSincePosted <= 7) ? (8 - (int)daysSincePosted) : 1;
			int commentCount = commentCounts.getOrDefault(feed.getFeedId(), 0);// map 조회로 변경 예정
			int likeCount = likeCounts.getOrDefault(feed.getFeedId(), 0); // map 조회로 변경 예정
			double popularityScore = calculatePopularityScore(feed.getViews(), commentCount, likeCount, weight);
			feed.updatePopularityScore(popularityScore);
		});

		// 상위 100개의 게시글을 인기 순으로 정렬하여 캐시에 저장
		List<Feed> top100Feeds = feeds.stream()
			.sorted(Comparator.comparingDouble(Feed::getPopularityScore).reversed())
			.limit(100)
			.toList();

		//Redis에 저장
		redisTemplate.opsForValue().set("popularFeeds", top100Feeds);
		//-----------------------------

		// 요청이 들어오면 top 3를(userId / feedId)포함해서 캐시에서 꺼내서 보내기

	}

	@Transactional
	public List<FeedTopResponseDto> getTopFeed(int limit) {
		// 캐시에서 인기 게시글 조회
		List<Feed> top100Feeds = redisTemplate.opsForValue().get("popularFeeds");
		if (top100Feeds == null || top100Feeds.isEmpty()) {
			throw new CustomException(ErrorCode.CACHE_NOT_FOUND);
		}

		Map<Long, Integer> commentCounts = interactionClient.getCommentCounts(
			top100Feeds.stream().map(Feed::getFeedId).toList()
		);
		Map<Long, Integer> likeCounts = interactionClient.getLikeCounts(
			top100Feeds.stream().map(Feed::getFeedId).toList()
		);

		// 상위 limit 개수만 조회하여 응답 DTO로 변환
		return top100Feeds.stream()
			.limit(limit)
			.map(feed -> FeedTopResponseDto.of(feed,
				commentCounts.getOrDefault(feed.getFeedId(), 0),
				likeCounts.getOrDefault(feed.getFeedId(), 0)))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Slice<FeedSearchResponseDto> searchFeeds(String title, String content, String region, Category category,
		Pageable pageable) {

		FeedSearchRequestDto searchRequestDto = FeedSearchRequestDto.builder()
			.title(title)
			.content(content)
			.region(region)
			.category(category)
			.build();

		return feedRepository.searchFeeds(searchRequestDto, pageable);
	}

	private double calculatePopularityScore(int views, long commentCount, long likeCount, int weight) {
		double popularityScore = (views * 0.2 + commentCount * 0.5 + likeCount * 0.3) * weight; // 

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

	public boolean checkFeedExists(Long feedId, Long userId) {
		return feedRepository.existsByIdAndUserId(feedId, userId);
	}

}