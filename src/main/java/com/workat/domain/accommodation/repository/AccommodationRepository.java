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

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, AccommodationCustomRepository {

	Optional<Accommodation> findByName(String name);

	Page<Accommodation> findAll(Pageable pageable);

	Page<Accommodation> findAllByRegionType(RegionType region, Pageable pageable);

	List<Accommodation> findAllByNameContaining(String name);

	@Query(value =
		"SELECT a.id, a.created_date, a.updated_date, a.region_type, a.accommodation_name, a.img_url, a.thumbnail_img_url, a.price, a.phone, a.road_address_name, a.place_url, a.related_url "
			+ "FROM accommodation as a JOIN accommodation_info as ai "
			+ "ON a.id = ai.accommodation_id "
			+ "WHERE (ai.tag IS NULL OR ai.tag = :infoTag)",
		nativeQuery = true)
	Page<Accommodation> findAllByInfoTag(
		@Param("infoTag") String infoTag,
		Pageable pageable
	);

	@Query(value =
		"SELECT a.id, a.created_date, a.updated_date, a.region_type, a.accommodation_name, a.img_url, a.thumbnail_img_url, a.price, a.phone, a.road_address_name, a.place_url, a.related_url "
			+ "FROM accommodation a JOIN accommodation_info ai "
			+ "ON a.id = ai.accommodation_id "
			+ "WHERE (a.region_type IS NULL OR a.region_type = :regionType) "
			+ "AND (ai.tag IS NULL OR ai.tag = :infoTag)",
		nativeQuery = true)
	Page<Accommodation> findAllByRegionAndInfoTag(
		@Param("regionType") String regionType,
		@Param("infoTag") String infoTag,
		Pageable pageable
	);

	@Query(value = "SELECT * FROM accommodation a ORDER BY RAND() LIMIT :limitNum",
		nativeQuery = true)
	List<Accommodation> findAllByRandom(int limitNum);
}
