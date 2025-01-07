package com.linkeleven.msa.area.infrastructure.naverplace;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkeleven.msa.area.application.dto.PlaceResponseDto;
import com.linkeleven.msa.area.application.service.SearchNaverService;
import com.linkeleven.msa.area.libs.exception.CustomException;
import com.linkeleven.msa.area.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchNaverPlaceServiceImpl implements SearchNaverService {

	@Value("${place.naver.client-id}")
	private String clientId;

	@Value("${place.naver.client-secret}")
	private String clientSecret;

	@Value("${place.naver.url}")
	private String placeURL;


	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;


	@Override
	public List<PlaceResponseDto> searchPlace(String query, int display, int start) {
		String url = placeURL +
			"?query=" + query +
			"&display=" + display +
			"&start=" + start;

		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.set("X-Naver-Client-Id", clientId);
		headers.set("X-Naver-Client-Secret", clientSecret);

		org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
		String response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class).getBody();

		return parseResponse(response);
	}

	private List<PlaceResponseDto> parseResponse(String response) {
		List<PlaceResponseDto> places = new ArrayList<>();
		try {
			JsonNode root = objectMapper.readTree(response);
			JsonNode items = root.get("items");

			if (items != null) {
				for (JsonNode item : items) {
					places.add(PlaceResponseDto.of(
						item.get("title").asText(),
						item.get("category").asText(),
						item.get("address").asText(),
						item.get("mapx").asText(),
						item.get("mapy").asText()
					));
				}
			}
		} catch (Exception e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		return places;
	}

}
