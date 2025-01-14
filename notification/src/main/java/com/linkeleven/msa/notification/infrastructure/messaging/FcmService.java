package com.linkeleven.msa.notification.infrastructure.messaging;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.linkeleven.msa.notification.libs.exception.CustomException;
import com.linkeleven.msa.notification.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

	private final FcmErrorMapper fcmErrorMapper;

	public void sendFcm(String token, String title, String body) {
		try {
			Message message = Message.builder()
				.setToken(token)
				.setNotification(createNotification(title, body))
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

	private Notification createNotification(String title, String body) {
		return Notification.builder()
			.setTitle(title)
			.setBody(body)
			.build();
	}
}
