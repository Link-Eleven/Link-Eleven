// package com.linkeleven.msa.notification.infrastructure.messaging;
//
// import org.springframework.stereotype.Component;
//
// import com.google.firebase.messaging.FirebaseMessagingException;
// import com.google.firebase.messaging.MessagingErrorCode;
// import com.linkeleven.msa.notification.libs.exception.CustomException;
// import com.linkeleven.msa.notification.libs.exception.ErrorCode;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Component
// public class FcmErrorMapper {
//
// 	public CustomException mappingFireBaseErrorCode(FirebaseMessagingException e) {
// 		MessagingErrorCode firebaseErrorCode = e.getMessagingErrorCode();
// 		com.google.firebase.ErrorCode errorCode = e.getErrorCode();
//
// 		log.error("파이어베이스 에러 발생 {}, {}",e.getMessagingErrorCode(), e.getErrorCode());
//
// 		if (firebaseErrorCode == null) {
// 			if (errorCode == com.google.firebase.ErrorCode.UNAUTHENTICATED) {
// 				return new CustomException(ErrorCode.UNAUTHORIZED);
// 			}
// 			return new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
// 		}
//
// 		switch (firebaseErrorCode) {
// 			case UNAVAILABLE -> {
// 				return new CustomException(ErrorCode.SERVER_UNAVAILABLE);
// 			}
// 			case INVALID_ARGUMENT -> {
// 				return new CustomException(ErrorCode.FIREBASE_INVALID_ARGUMENT);
// 			}
// 			case UNREGISTERED -> {
// 				return new CustomException(ErrorCode.FIREBASE_TOKEN_UNREGISTERED);
// 			}
// 			case QUOTA_EXCEEDED -> {
// 				return new CustomException(ErrorCode.FIREBASE_QUOTA_EXCEEDED);
// 			}
// 			case SENDER_ID_MISMATCH -> {
// 				return new CustomException(ErrorCode.FIREBASE_SENDER_ID_MISMATCH);
// 			}
// 			case THIRD_PARTY_AUTH_ERROR -> {
// 				return new CustomException(ErrorCode.FIREBASE_THIRD_PARTY_AUTH_ERROR);
// 			}
// 			default -> {
// 				return new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
// 			}
// 		}
// 	}
// }
