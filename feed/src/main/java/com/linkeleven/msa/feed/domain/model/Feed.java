package com.linkeleven.msa.feed.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_feed")
public class Feed extends BaseTime {

	@Id
	@Tsid
	private Long feedId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "location_id")
	private Long locationId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "category", nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	@Builder.Default
	@Column(name = "views", nullable = false)
	private int views = 0;

	@Builder.Default
	@Column(name = "popularity_score", nullable = false)
	private Double popularityScore = 0.0;

	public static Feed of(Long userId, Long locationId, String title, String content, Category category) {
		return Feed.builder()
			.userId(userId)
			.locationId(locationId)
			.title(title)
			.content(content)
			.category(category)
			.build();
	}

	public void update(String title, String content, Category category) {
		this.title = title;
		this.content = content;
		this.category = category;
	}
}
