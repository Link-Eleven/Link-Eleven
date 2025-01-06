package com.linkeleven.msa.area.domain.entity;

import com.linkeleven.msa.area.domain.common.CategoryType;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_category")
public class Category extends BaseTime{
	@Id
	@Tsid
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private CategoryType categoryType;

	@Column(name = "category_name", nullable = false)
	private String categoryName;


	public static Category createCategory(CategoryType type, String categoryName){
		return Category.builder()
			.categoryType(type)
			.categoryName(categoryName)
			.build();
	}


}
