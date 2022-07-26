package com.workat.domain.config;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.chat.repository.room.ChatRoomCustomRepository;
import com.workat.domain.chat.repository.room.ChatRoomCustomRepositoryImpl;

@EnableJpaAuditing
@TestConfiguration
public class DataJpaTestConfig {

	@Autowired
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
