package com.linkeleven.msa.notification.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class AwsConfig {

	@Value("${sns.topic.arn}")
	private String snsTopicArn;

	@Value("${aws.accessKey}")
	private String awsAccessKey;

	@Value("${aws.secretKey}")
	private String awsSecretKey;

	@Value("${aws.region}")
	private String awsRegion;


}
