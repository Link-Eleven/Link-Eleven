package com.linkeleven.msa.recommendation.domain.model;

import java.util.ArrayList;
import java.util.List;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_recommendation")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation extends BaseTime {
	@Id
	@Tsid
	private Long recommendationId;

	private Long userId;

	@ElementCollection
	@CollectionTable(
		name = "p_recommendation_keywords",
		joinColumns = @JoinColumn(name = "recommendation_id")
	)
	private List<String> keywords = new ArrayList<>();

	@Setter
	@Column(name = "created_by", updatable = false)
	private Long createdBy;

	@Setter
	@Column(name = "updated_by")
	private Long updatedBy;

	public static Recommendation of(Long userId, List<String> keywords) {
		return Recommendation.builder()
			.userId(userId)
			.createdBy(userId)
			.updatedBy(userId)
			.keywords(keywords)
			.build();
	}

	public void updateKeywords(List<String> newKeywords) {
		this.keywords = newKeywords;
	}
}
