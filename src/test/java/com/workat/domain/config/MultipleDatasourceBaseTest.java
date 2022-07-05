package com.workat.domain.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MultipleDatasourceBaseTest {
	static final String MYSQL_IMAGE = "mysql:5.7.37";
	static final MySQLContainer<?> MYSQL_CONTAINER;

	static final String REDIS_IMAGE = "redis:6-alpine";
	static final GenericContainer REDIS_CONTAINER;

	static {
		MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_IMAGE)
			.withDatabaseName("work_at")
			.withUsername("admin")
			.withPassword("admin");
		MYSQL_CONTAINER.start();

		REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
			.withExposedPorts(6379)
			.withEnv("password", "password")
			.withReuse(true);
		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",MYSQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);

		registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(6379));
		registry.add("spring.redis.password", () -> "" + REDIS_CONTAINER.getEnv().get(0));
	}

}
