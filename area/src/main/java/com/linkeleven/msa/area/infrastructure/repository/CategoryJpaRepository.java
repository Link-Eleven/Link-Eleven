package com.linkeleven.msa.area.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkeleven.msa.area.domain.entity.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

	@Query("SELECT c FROM Category c WHERE c.categoryName IN :categoryNameList")
	List<Category> findAllCategoryInCategoryName(@Param("categoryNameList") List<String> categoryNameList);
}
