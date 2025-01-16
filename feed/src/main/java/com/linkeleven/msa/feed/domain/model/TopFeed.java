package com.linkeleven.msa.feed.domain.model;

import java.time.LocalDateTime;

import com.linkeleven.msa.feed.application.dto.FeedTopResponseDto;

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
@Table(name = "p_top_feed")
public class TopFeed {

	@Id
	@Tsid
	private Long topFeedId;

	@Column(name = "feed_id", nullable = false)
	private Long feedId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "popularity_score", nullable = false)
	private double popularityScore;

	@Column(name = "comment_count", nullable = false)
	private long commentCount;

	@Column(name = "like_count", nullable = false)
	private long likeCount;

	@Column(name = "backup_date", nullable = false)
	private LocalDateTime backupDate;

	public static TopFeed of(FeedTopResponseDto dto) {
		if (dto == null) {
			throw new IllegalArgumentException("FeedTopResponseDto cannot be null");
		}
		return TopFeed.builder()
			.feedId(dto.getFeedId())
			.userId(dto.getUserId())
			.title(dto.getTitle())
			.popularityScore(dto.getPopularityScore())
			.commentCount(dto.getCommentCount())
			.likeCount(dto.getLikeCount())
			.backupDate(LocalDateTime.now())
			.build();
	}

	public FeedTopResponseDto toDto() {
		return FeedTopResponseDto.builder()
			.feedId(this.feedId)
			.userId(this.userId)
			.title(this.title)
			.commentCount(this.commentCount)
			.likeCount(this.likeCount)
			.popularityScore(this.popularityScore)
			.build();
	}
}
