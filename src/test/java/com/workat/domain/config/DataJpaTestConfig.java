package com.workat.domain.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.api.map.service.LocationImageGenerator;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@TestConfiguration
public class DataJpaTestConfig {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public LocationImageGenerator imageGenerator() {
		return new LocationImageGenerator();
	}
}
