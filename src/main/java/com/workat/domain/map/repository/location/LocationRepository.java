package com.workat.domain.map.repository.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;

public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {

	Optional<Location> findByPlaceId(String placeId);

	List<Location> findAllByType(LocationType category);
}
