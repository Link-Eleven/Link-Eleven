package com.linkeleven.msa.notification.application;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.notification.application.service.AwsService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@RequiredArgsConstructor
public class AwsNotificationService {

	private final AwsService awsService;

	public void sendAwsNotification(Long targetUserId, String message) {
		String endpoint = getWebhookUrl(targetUserId);

		SnsClient snsClient = awsService.getSnsClient();

		PublishRequest publishRequest = PublishRequest.builder()
			.topicArn("arn:aws:sns:ap-northeast-2:509428312681:linkeleven")
			.subject("LINK ELEVEN NOTIFICATION")
			.message(message)
			.build();

		snsClient.publish(publishRequest);
	}

	private String getWebhookUrl(Long targetUserId) {
		return "https://webhook.site/a3b7e3a8-ed2e-410e-bb52-9fcd074e8f20";
	}
}
