package com.workat.domain.map.repository.location;

import java.util.List;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.vo.MapPoint;

public interface LocationRepositoryCustom {

	List<Location> findAllByRadius(MapPoint minPoint, MapPoint maxPoint);
}
