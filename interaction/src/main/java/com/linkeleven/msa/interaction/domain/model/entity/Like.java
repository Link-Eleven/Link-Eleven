package com.linkeleven.msa.interaction.domain.model.entity;

import com.linkeleven.msa.interaction.domain.model.vo.Target;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_like")
public class Like extends BaseTime{

	@Id
	@Tsid
	private Long id;

	@Embedded
	@Column
	private Target target;

	@Column(nullable = false)
	private Long userId;

	@Builder
	private Like(Target target, Long userId) {
		this.target = target;
		this.userId = userId;
	}

	public static Like of(Target target, Long userId) {
		return Like.builder()
			.target(target)
			.userId(userId)
			.build();
	}
}
