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
	private Long id;

	@Column
	private Long userId;

	@Column
	private Long areaId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	@Builder.Default
	@Column(nullable = false)
	private int views = 0;

	@Builder.Default
	@Column(nullable = false)
	private Double popularityScore = 0.0;

}
