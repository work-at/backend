package com.workat.domain.config;

import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MysqlContainerBaseTest {
	static final String MYSQL_IMAGE = "mysql:5.7.37";
	static final MySQLContainer<?> MYSQL_CONTAINER;

	static {
		MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_IMAGE)
			.withDatabaseName("work_at")
			.withUsername("admin")
			.withPassword("admin");
		MYSQL_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",MYSQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
	}

}
