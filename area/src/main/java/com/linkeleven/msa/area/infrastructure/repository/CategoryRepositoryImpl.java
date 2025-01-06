package com.linkeleven.msa.area.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.linkeleven.msa.area.domain.entity.Category;
import com.linkeleven.msa.area.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
	private final CategoryJpaRepository categoryJpaRepository;

	@Override
	public List<Category> findAllCategoryInCategoryName(List<String> categoryNameList) {
		return categoryJpaRepository.findAllCategoryInCategoryName(categoryNameList);
	}

	@Override
	public List<Category> saveAll(List<Category> categoryList) {
		return categoryJpaRepository.saveAll(categoryList);
	}
}
