package com.linkeleven.msa.interaction.presentation.enums;

public enum SortBy {
	LIKE,
	CREATED_AT;

	public static SortBy fromString(String input) {
		if (input == null || !input.equalsIgnoreCase("LIKE")) {
			return CREATED_AT;
		}
		return LIKE;
	}
}
