package com.workat.domain.map.repository.worker;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.workat.api.map.dto.WorkerPinDto;
import com.workat.domain.map.entity.WorkerLocation;

public interface WorkerLocationRedisRepository extends CrudRepository<WorkerLocation, Long> {

	@Query(
		value = "GEORADIUS worker:location :point :kilometer km WITHDIST",
		nativeQuery = true)
	List<WorkerPinDto> findWorkerPinsByLocationNear(Point point, double kilometer);

	List<WorkerLocation> findAllByLocationNear(Point point, Distance distance);
}
