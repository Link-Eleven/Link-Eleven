package com.linkeleven.msa.notification.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.notification.infrastructure.config.AwsConfig;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@RequiredArgsConstructor
public class AwsService {

	private final AwsConfig awsConfig;

	public AwsCredentialsProvider getAwsCredentials(String accessKey, String secretKey) {
		AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
		return () -> awsBasicCredentials;
	}

	public SnsClient getSnsClient() {
		return SnsClient.builder()
			.credentialsProvider(
				getAwsCredentials(awsConfig.getAwsAccessKey(), awsConfig.getAwsSecretKey())
			).region(Region.of(awsConfig.getAwsRegion()))
			.build();
	}


}
