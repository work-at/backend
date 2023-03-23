package com.workat.domain.accommodation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.accommodation.entity.Accommodation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AccommodationCustomRepositoryImpl implements AccommodationCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Accommodation> findRandom(int num) {
		return List.of();
	}
}
