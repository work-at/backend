package com.workat.domain.accommodation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.AccommodationInfo;

public interface AccommodationInfoRepository extends JpaRepository<AccommodationInfo, Long> {

	List<AccommodationInfo> findAllByAccommodation(Accommodation accommodation);

}
