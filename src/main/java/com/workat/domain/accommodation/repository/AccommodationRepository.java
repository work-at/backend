package com.workat.domain.accommodation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.entity.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

	Page<Accommodation> findAllByRegionType(RegionType regionType, Pageable pageable);

	List<Accommodation> findAllByName(String name);

	Optional<Accommodation> findById(Long accommodationId);

}
