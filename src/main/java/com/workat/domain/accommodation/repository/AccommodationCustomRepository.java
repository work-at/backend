package com.workat.domain.accommodation.repository;

import com.workat.domain.accommodation.entity.Accommodation;
import java.util.List;

public interface AccommodationCustomRepository {

	List<Accommodation> findRandom(int num);
}
