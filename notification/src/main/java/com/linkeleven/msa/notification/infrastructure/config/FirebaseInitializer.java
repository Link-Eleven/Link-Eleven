package com.linkeleven.msa.notification.infrastructure.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.linkeleven.msa.notification.libs.exception.CustomException;
import com.linkeleven.msa.notification.libs.exception.ErrorCode;

@Configuration
public class FirebaseInitializer {

	@Value("${firebase.config-path}")
	private String firebaseConfigPath;

	public void initializeFirebase() {
		try {
			ClassPathResource serviceAccount = new ClassPathResource(firebaseConfigPath);

			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
				.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomException(ErrorCode.FIREBASE_ERROR);
		}
	}
}
