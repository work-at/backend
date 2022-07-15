package com.workat.domain.map.repository.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;

public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {

	Optional<Location> findByPlaceId(String placeId);

	List<Location> findAllByCategory(LocationCategory category);
}
