package com.workat.domain.map.repository.location;

import java.util.Collections;
import java.util.List;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.vo.MapPoint;

public class LocationRepositoryCustomImpl implements LocationRepositoryCustom {

	// TODO: 2022/07/13 수요일 또는 목요일까지 구현 완료 예정
	@Override
	public List<Location> findAllByRadius(MapPoint minPoint, MapPoint maxPoint) {
		return Collections.emptyList();
	}
}
