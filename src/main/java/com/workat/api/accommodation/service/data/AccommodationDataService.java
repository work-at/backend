package com.workat.api.accommodation.service.data;

import com.workat.common.exception.NotFoundException;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.AccommodationReview;
import com.workat.domain.accommodation.entity.review.AccommodationReviewCounting;
import com.workat.domain.accommodation.entity.review.AccommodationReviewHistory;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.accommodation.repository.AccommodationRepository;
import com.workat.domain.accommodation.repository.review.AccommodationReviewRepository;
import com.workat.domain.accommodation.repository.review.counting.AccommodationReviewCountingRepository;
import com.workat.domain.accommodation.repository.review.history.AccommodationReviewHistoryRepository;
import com.workat.domain.user.entity.Users;
import java.lang.reflect.Field;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccommodationDataService {

	private final AccommodationRepository accommodationRepository;

	private final AccommodationReviewRepository accommodationReviewRepository;

	private final AccommodationReviewCountingRepository accommodationReviewCountingRepository;

	private final AccommodationReviewHistoryRepository accommodationReviewHistoryRepository;

	public Accommodation saveAccommodation(Accommodation accommodation) {
		return this.accommodationRepository.save(accommodation);
	}

	public AccommodationReview saveAccommodationReview(AccommodationReview accommodationReview) {
		return this.accommodationReviewRepository.save(accommodationReview);
	}

	public AccommodationReviewHistory saveAccommdoationReviewHistory(AccommodationReviewHistory accommodationReviewHistory) {
		return this.accommodationReviewHistoryRepository.save(accommodationReviewHistory);
	}

	public Accommodation getAccommodation(long accommodationId) {
		return accommodationRepository.findById(accommodationId).orElseThrow(() -> {
			throw new NotFoundException("accommodation is not found(id: " + accommodationId + " )");
		});
	}

	public List<Accommodation> getRandomAccommodation(int num) {
		return accommodationRepository.findRandom(num);
	}

	public boolean isExistAccommodationReviewHistoryByStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status) {
		return accommodationReviewHistoryRepository.existAccommodationReviewHistoryMatchingStatus(user, accommodation, status);
	}

	public AccommodationReviewHistory getLatestAccommodationReviewHistory(Users user, Accommodation accommodation) {
		return accommodationReviewHistoryRepository.latestAccommodationReviewHistory(user, accommodation)
			.orElseThrow(() -> {
				throw new NotFoundException("not found AccommodationReviewHistory, user(id : " + user.getId() + " , accommodation(id: " + accommodation.getId() + ")");
			});
	}

	public List<Accommodation> findAllAccommodation() {
		return accommodationRepository.findAll();
	}

	public List<Accommodation> findAllByNameContaining(String name) {
		return accommodationRepository.findAllByNameContaining(name);
	}

	public void saveAllAccommodationReviewCounting(List<AccommodationReviewCounting> countingInfoList) {
		this.accommodationReviewCountingRepository.saveAll(countingInfoList);
	}
}
