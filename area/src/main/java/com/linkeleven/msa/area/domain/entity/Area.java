package com.linkeleven.msa.area.domain.entity;

import com.linkeleven.msa.area.domain.vo.Region;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
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
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_area")
public class Area {
	@Id
	@Tsid
	private Long id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "code", column = @Column(name = "code", nullable = false)),
		@AttributeOverride(name = "sido", column = @Column(name = "sido", nullable = false)),
		@AttributeOverride(name = "sigungu", column = @Column(name = "sigungu")),
		@AttributeOverride(name = "eupmyeondong", column = @Column(name = "eupmyeondong")),
		@AttributeOverride(name = "ri", column = @Column(name = "ri"))
	})
	private Region region;



}
