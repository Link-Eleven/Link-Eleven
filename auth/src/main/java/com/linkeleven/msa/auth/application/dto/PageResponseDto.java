package com.linkeleven.msa.auth.application.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {
	private List<T> queryList;
	private boolean hasNext;

	public static <T> PageResponseDto<T> from(Slice<T> slice) {
		return PageResponseDto.<T>builder()
			.queryList(slice.getContent())
			.hasNext(slice.hasNext())
			.build();
	}
}