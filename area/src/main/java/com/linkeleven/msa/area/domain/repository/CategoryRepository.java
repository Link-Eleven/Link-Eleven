package com.linkeleven.msa.area.domain.repository;

import java.util.List;

import com.linkeleven.msa.area.domain.entity.Category;

public interface CategoryRepository {

	List<Category> findAllCategoryInCategoryName(List<String> categoryNameList);

	List<Category> saveAll(List<Category> categoryList);
}
