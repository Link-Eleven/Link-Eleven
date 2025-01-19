package com.linkeleven.msa.feed.domain.model;

import java.time.LocalDateTime;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Setter
	@Column(name = "comment_count", nullable = false)
	private long commentCount;

	@Setter
	@Column(name = "like_count", nullable = false)
	private long likeCount;

	@Column(name = "views", nullable = false)
	private int views;

	@Column(name = "popularity_score", nullable = false)
	private double popularityScore;

	@Column(name = "backup_date", nullable = false)
	private LocalDateTime backupDate;

	public static TopFeed of(Long feedId, Long userId, String title, long commentCount,
		long likeCount, int views, double popularityScore) {
		return TopFeed.builder()
			.feedId(feedId)
			.userId(userId)
			.title(title)
			.commentCount(commentCount)
			.likeCount(likeCount)
			.views(views)
			.popularityScore(popularityScore)
			.backupDate(LocalDateTime.now())
			.build();
	}

	public void setViewCount(int viewCount) {
		this.views = viewCount;
	}
}
