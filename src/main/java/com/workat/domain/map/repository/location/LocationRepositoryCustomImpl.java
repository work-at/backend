package com.workat.domain.map.repository.location;

import static com.workat.domain.map.entity.QLocation.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.vo.MapRangeInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocationRepositoryCustomImpl implements LocationRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Location> findAllByRadius(LocationCategory category, MapRangeInfo mapRangeInfo) {
		return jpaQueryFactory.selectFrom(location)
			.where(intoMapRange(mapRangeInfo).and(location.category.eq(category)))
			.fetch();
	}

	private BooleanExpression intoMapRange(MapRangeInfo mapRangeInfo) {
		return location.latitude.goe(mapRangeInfo.getMinLatitude())
			.and(location.latitude.loe(mapRangeInfo.getMaxLatitude()))
			.and(location.longitude.goe(mapRangeInfo.getMinLongitude()))
			.and(location.longitude.loe(mapRangeInfo.getMaxLongitude()));
	}
}
