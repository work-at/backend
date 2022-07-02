package com.workat.domain.map.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;

public interface LocationRepository extends JpaRepository<Location, Long> {

	Optional<Location> findByPlaceId(String placeId);

	Optional<List<Location>> findAllByCategory(LocationCategory category);
}
