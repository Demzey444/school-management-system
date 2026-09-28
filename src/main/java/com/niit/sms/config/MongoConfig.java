package com.niit.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // Enables @CreatedDate and @LastModifiedDate.
    // MongoTemplate is auto-configured by Spring Boot using spring.data.mongodb.uri.
}
