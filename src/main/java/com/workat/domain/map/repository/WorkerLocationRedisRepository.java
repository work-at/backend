package com.workat.domain.map.repository;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;

import com.workat.domain.map.entity.WorkerLocation;

public interface WorkerLocationRedisRepository extends CrudRepository<WorkerLocation, Long> {

	List<WorkerLocation> findAllByLocationNear(Point point, Distance distance);
}
