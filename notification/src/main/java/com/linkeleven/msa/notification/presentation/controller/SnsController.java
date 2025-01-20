// package com.linkeleven.msa.notification.presentation.controller;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.linkeleven.msa.notification.application.service.AwsService;
// import com.linkeleven.msa.notification.infrastructure.config.AwsConfig;
// import com.linkeleven.msa.notification.libs.exception.CustomException;
// import com.linkeleven.msa.notification.libs.exception.ErrorCode;
//
// import lombok.RequiredArgsConstructor;
// import software.amazon.awssdk.services.sns.SnsClient;
// import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
// import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
// import software.amazon.awssdk.services.sns.model.SubscribeRequest;
// import software.amazon.awssdk.services.sns.model.SubscribeResponse;
//
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/notification")
// public class SnsController {
//
// 	private final AwsConfig awsConfig;
// 	private final AwsService awsService;
//
// 	@PostMapping("/create-topic")
// 	public ResponseEntity<String> createTopic(
// 		@RequestParam final String topicName
// 	) {
// 		final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
// 			.name(topicName)
// 			.build();
//
// 		SnsClient snsClient = awsService.getSnsClient();
// 		final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);
//
// 		if (!createTopicResponse.sdkHttpResponse().isSuccessful()) {
// 			throw new CustomException(ErrorCode.FIREBASE_ERROR);
// 		}
//
// 		return new ResponseEntity<>("토픽 생성", HttpStatus.OK);
// 	}
//
// 	@PostMapping("/subs")
// 	public ResponseEntity<String> subscribe(
// 		@RequestParam final String endpoint,
// 		@RequestParam final String topicArn
// 	) {
// 		final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
// 			.protocol("https")
// 			.topicArn(topicArn)
// 			.endpoint(endpoint)
// 			.build();
//
// 		SnsClient snsClient = awsService.getSnsClient();
// 		final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);
//
// 		if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
// 			throw new CustomException(ErrorCode.FIREBASE_THIRD_PARTY_AUTH_ERROR);
// 		}
//
// 		return new ResponseEntity<>("구독", HttpStatus.OK);
// 	}
// }
