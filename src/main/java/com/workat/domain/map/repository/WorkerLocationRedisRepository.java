package com.workat.domain.map.repository;

import org.springframework.data.repository.CrudRepository;

import com.workat.domain.map.entity.WorkerLocation;

public interface WorkerLocationRedisRepository extends CrudRepository<WorkerLocation, String> {
}
