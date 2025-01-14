package com.linkeleven.msa.notification.infrastructure.messaging;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.linkeleven.msa.notification.libs.exception.CustomException;
import com.linkeleven.msa.notification.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

	private final FcmErrorMapper fcmErrorMapper;

	public void sendFcm(String token, String body, Long targetId, String contentType) {
		try {
			Message message = Message.builder()
				.setToken(token)
				.setWebpushConfig(createWebPushConfig())
				.setNotification(createNotification(body))
				.putData("targetId", String.valueOf(targetId))
				.putData("notificationType", contentType)
				.putData("timestamp", String.valueOf(System.currentTimeMillis()))
				.build();

			String fcmResponse = FirebaseMessaging.getInstance().send(message);
			log.info("FCM으로 발송 완료 msg : {}, fcmResponse : {} ", message, fcmResponse);

		} catch (FirebaseMessagingException e) {
			throw fcmErrorMapper.mappingFireBaseErrorCode(e);
		} catch (Exception e) {
			log.error("일반 에러 발생 {}", e.getMessage(), e);
			throw new CustomException(ErrorCode.FIREBASE_ERROR);
		}
	}

	private Notification createNotification(String body) {
		return Notification.builder()
			.setTitle("Link Eleven 알림")
			.setBody(body)
			.build();
	}

	private WebpushConfig createWebPushConfig() {
		return WebpushConfig.builder()
			.putHeader("TTL", "300")
			.build();
	}
}
