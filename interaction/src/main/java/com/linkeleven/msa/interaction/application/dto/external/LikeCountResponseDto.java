package com.linkeleven.msa.interaction.application.dto.external;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeCountResponseDto {

	private Map<Long, Integer> count;

}
