package com.workat.domain.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

public abstract class RedisContainerBaseTest {
	static final String REDIS_IMAGE = "redis:6-alpine";
	static final GenericContainer REDIS_CONTAINER;

	static {
		REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
			.withExposedPorts(6379)
			.withEnv("password", "password")
			.withReuse(true);
		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(6379));
		registry.add("spring.redis.password", () -> "" + REDIS_CONTAINER.getEnv().get(0));
	}
}
