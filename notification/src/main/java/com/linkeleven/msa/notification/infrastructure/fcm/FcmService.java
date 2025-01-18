// package com.linkeleven.msa.notification.infrastructure.messaging;
//
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Service;
//
// import com.google.firebase.messaging.FirebaseMessaging;
// import com.google.firebase.messaging.Message;
// import com.google.firebase.messaging.Notification;
// import com.google.firebase.messaging.WebpushConfig;
//
// import lombok.RequiredArgsConstructor;
// import lombok.SneakyThrows;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class FcmService {
//
// 	private final FcmErrorMapper fcmErrorMapper;
//
// 	@Async
// 	@SneakyThrows
// 	public void sendFcm(String token, String body, Long targetId, String contentType) {
// 		// try {
// 			Message message = Message.builder()
// 				.setToken("APA91bH6RyOK8Qj2EPM2pZZVtiLs9QL7W73blb1qvgj3Udk7KmeKjDPqPqzYePNxE7Kd8X7w4XMwG91rG22Z8bOeDmZuFRH5pO67IYxsWpN")
// 				.setNotification(createNotification(body))
// 				.setWebpushConfig(createWebPushConfig())
// 				.putData("targetId", String.valueOf(targetId))
// 				.putData("notificationType", contentType)
// 				.putData("timestamp", String.valueOf(System.currentTimeMillis()))
// 				.build();
// 			log.debug("Sending FCM with Token: {}, Body: {}, TargetId: {}, ContentType: {}", token, body, targetId, contentType);
//
// 			String fcmResponse = FirebaseMessaging.getInstance().send(message);
// 			log.info("FCM으로 발송 완료 msg : {}, fcmResponse : {} ", message, fcmResponse);
//
// 		// } catch (FirebaseMessagingException e) {
// 		// 	throw fcmErrorMapper.mappingFireBaseErrorCode(e);
// 		// } catch (Exception e) {
// 		// 	log.error("일반 에러 발생 {}", e.getMessage(), e);
// 		// 	throw new CustomException(ErrorCode.FIREBASE_ERROR);
// 		// }
// 	}
//
// 	private Notification createNotification(String body) {
// 		return Notification.builder()
// 			.setTitle("Link Eleven 알림")
// 			.setBody(body)
// 			.build();
// 	}
//
// 	private WebpushConfig createWebPushConfig() {
// 		return WebpushConfig.builder()
// 			.putHeader("TTL", "300")
// 			.build();
// 	}
// }
