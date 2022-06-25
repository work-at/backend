package com.workat.domain.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.map.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
