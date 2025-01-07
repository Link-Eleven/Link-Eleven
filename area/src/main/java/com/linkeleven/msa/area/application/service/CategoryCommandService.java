package com.linkeleven.msa.area.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.area.application.dto.PlaceResponseDto;
import com.linkeleven.msa.area.domain.common.CategoryType;
import com.linkeleven.msa.area.domain.entity.Category;
import com.linkeleven.msa.area.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryCommandService {
	private final CategoryRepository categoryRepository;

	@Transactional
	public List<Category> createCategoryList(List<PlaceResponseDto> placeResponseDtoList) {
		List<Category> existcategoryList = findExistedCategory(placeResponseDtoList);

		Set<String> uniqueCategories = new HashSet<>();

		List<Category> newCategoryList = placeResponseDtoList.stream()
			.filter(placeResponseDto -> {
				return existcategoryList.stream()
					.noneMatch(category -> placeResponseDto.getCategory().equals(category.getCategoryName()))
					&& uniqueCategories.add(placeResponseDto.getCategory());
			})
			.map(placeResponseDto -> {
				CategoryType categoryType = matchCategoryType(placeResponseDto.getCategory());
				return Category.createCategory(categoryType, placeResponseDto.getCategory());
			})
			.toList();

		List<Category> categoryList = new ArrayList<>(existcategoryList);
		categoryList.addAll(newCategoryList);

		return categoryRepository.saveAll(categoryList);
	}

	private List<Category> findExistedCategory(List<PlaceResponseDto> placeResponseDtoList) {
		List<String> categoryNameList = placeResponseDtoList.stream().map(PlaceResponseDto::getCategory).toList();
		return categoryRepository.findAllCategoryInCategoryName(categoryNameList);
	}

	// 초기 데이터 분리를 위한 임시 메서드 사용
	private CategoryType matchCategoryType(String category) {
		if (category.contains("음식점") || category.contains("한식") || category.contains("일식") || category.contains("양식")) {
			return CategoryType.RESTAURANT; // 음식점
		} else if (category.contains("호텔") || category.contains("콘도") || category.contains("리조트")) {
			return CategoryType.HOTEL; // 숙박
		} else if (category.contains("여행") || category.contains("명소") || category.contains("해수욕장") || category.contains(
			"축제") || category.contains("행사") || category.contains("온천")) {
			return CategoryType.PLACE; // 명소
		} else {
			return CategoryType.OTHER; // 기타
		}

	}
}
