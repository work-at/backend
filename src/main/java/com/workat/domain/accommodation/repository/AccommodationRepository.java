package com.workat.domain.accommodation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.entity.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

	Page<Accommodation> findAll(Pageable pageable);

	Page<Accommodation> findAllByRegionType(RegionType region, Pageable pageable);

	@Query(value = "SELECT *"
		+ "FROM accommodation a JOIN accommodation_info ai ON a.id = ai.accommodation_id "
		+ "WHERE (a.region_type IS NULL OR a.region_type = :regionType)"
		+ "AND (ai.tag IS NULL OR ai.tag = :infoTag) ",
		nativeQuery = true)
	Page<Accommodation> findAllByRegionAndInfoTag(
		@Param("regionType") String regionType,
		@Param("infoTag") String infoTag,
		Pageable pageable
	);

	List<Accommodation> findAllByName(String name);

	Optional<Accommodation> findById(Long accommodationId);

}
