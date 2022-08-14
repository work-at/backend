package com.workat.domain.map.repository.location;

import java.util.List;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.vo.MapPoint;
import com.workat.domain.map.vo.MapRangeInfo;

public interface LocationRepositoryCustom {

	List<Location> findAllByRadius(LocationCategory category, MapRangeInfo mapRangeInfo);
}
