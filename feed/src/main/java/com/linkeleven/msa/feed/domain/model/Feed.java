package com.linkeleven.msa.feed.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.linkeleven.msa.feed.domain.enums.Category;
import com.linkeleven.msa.feed.domain.enums.Region;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

	@Column(name = "region", nullable = false)
	@Enumerated(EnumType.STRING)
	private Region region;

	@Builder.Default
	@Column(name = "views", nullable = false)
	private int views = 0;

	@Builder.Default
	@Column(name = "popularity_score", nullable = false)
	private double popularityScore = 0.0;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "feed_id")
	@Builder.Default
	private List<File> files = new ArrayList<>();

	public static Feed of(Long userId, Long locationId, String title, String content, Category category,
		Region region) {
		return Feed.builder()
			.userId(userId)
			.locationId(locationId)
			.title(title)
			.content(content)
			.category(category)
			.region(region)
			.build();
	}

	public void update(String title, String content, Category category) {
		this.title = Optional.ofNullable(title).orElse(this.title);
		this.content = Optional.ofNullable(content).orElse(this.content);
		this.category = Optional.ofNullable(category).orElse(this.category);
	}

	public void updatePopularityScore(double popularityScore) {
		this.popularityScore = popularityScore;
	}

	public void delete(Long userId) {
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId);
		this.files.forEach(file -> file.delete(userId));
	}

}
