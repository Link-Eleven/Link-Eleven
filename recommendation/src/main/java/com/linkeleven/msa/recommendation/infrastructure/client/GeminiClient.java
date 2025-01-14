package com.linkeleven.msa.recommendation.infrastructure.client;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkeleven.msa.recommendation.domain.model.FeedLog;
import com.linkeleven.msa.recommendation.domain.model.Recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiClient {
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${gemini.api.key}")
	private String apiKey;

	private static final String GEMINI_API_URL =
		"https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent";

	public Recommendation processLogs(List<FeedLog> logs) {
		try {
			String titles = logs.stream()
				.map(FeedLog::getFeedTitle)
				.collect(Collectors.joining("\n"));

			String prompt = String.format(
				"""
					다음 게시글 제목들에서 가장 연관성이 높은 키워드 3개를 추출해주세요.
					중요도 순서대로 나열해주시고, 쉼표로 구분된 형태로만 응답해주세요.
					예시 응답 형식: 키워드1,키워드2,키워드3
					
					게시글 제목:
					%s
					""", titles);

			String requestBody = String.format("""
				{
				    "contents": [{
				        "parts":[{
				            "text": "%s"
				        }]
				    }]
				}
				""", prompt);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

			String response = restTemplate.postForObject(
				GEMINI_API_URL + "?key=" + apiKey,
				request,
				String.class
			);

			JsonNode jsonResponse = objectMapper.readTree(response);
			String result = jsonResponse
				.path("candidates")
				.get(0)
				.path("content")
				.path("parts")
				.get(0)
				.path("text")
				.asText();

			List<String> keywords = Arrays.asList(result.trim().split(","));
			Long userId = logs.get(0).getUserId();

			return Recommendation.of(userId, keywords);
		} catch (Exception e) {
			log.error("Failed to process logs with Gemini API", e);
			throw new RuntimeException("Gemini API 처리 중 오류가 발생했습니다", e);
		}
	}
}
