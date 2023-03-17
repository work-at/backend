package com.workat.api.accommodation.service.data;

import com.workat.common.exception.NotFoundException;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.AccommodationReview;
import com.workat.domain.accommodation.entity.review.AccommodationReviewHistory;
import com.workat.domain.accommodation.entity.review.abbreviation.AccommodationReviewAbbreviation;
import com.workat.domain.accommodation.entity.review.abbreviation.AccommodationReviewAbbreviationHistory;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.accommodation.repository.AccommodationRepository;
import com.workat.domain.accommodation.repository.review.AccommodationReviewRepository;
import com.workat.domain.accommodation.repository.review.history.AccommodationReviewHistoryRepository;
import com.workat.domain.accommodation.repository.review.history.abbreviation.AccommodationReviewAbbreviationRepository;
import com.workat.domain.accommodation.repository.review.history.abbreviation.AccommodationReviewAbbreviationHistoryRepository;
import com.workat.domain.user.entity.Users;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
 * TODO
 * 없애고 더 효율적인 방법을 찾아보자
 */
@RequiredArgsConstructor
@Component
public class AccommodationDataService {

	private final AccommodationRepository accommodationRepository;

	private final AccommodationReviewRepository accommodationReviewRepository;

	private final AccommodationReviewHistoryRepository accommodationReviewHistoryRepository;

	private final AccommodationReviewAbbreviationRepository accommodationReviewAbbreviationRepository;

	private final AccommodationReviewAbbreviationHistoryRepository accommodationReviewAbbreviationHistoryRepository;

	/*
	 * Accommodation
	 */
	public Accommodation saveAccommodation(Accommodation accommodation) {
		return this.accommodationRepository.save(accommodation);
	}

	public Accommodation getAccommodation(long accommodationId) {
		return accommodationRepository.findById(accommodationId).orElseThrow(() -> {
			throw new NotFoundException("accommodation is not found(id: " + accommodationId + " )");
		});
	}

	public List<Accommodation> getRandomAccommodation(int num) {
		return accommodationRepository.findRandom(num);
	}

	public List<Accommodation> getAllAccommodation() {
		return accommodationRepository.findAll();
	}

	public List<Accommodation> getAllByNameContaining(String name) {
		return accommodationRepository.findAllByNameContaining(name);
	}

	/*
	 * AccommodationReview
	 */
	public AccommodationReview saveAccommodationReview(AccommodationReview accommodationReview) {
		return this.accommodationReviewRepository.save(accommodationReview);
	}

	/*
	 * AccommodationReviewHistory
	 */
	public List<AccommodationReviewHistory> saveAccommodationReviewHistory(List<AccommodationReviewHistory> accommodationReviewHistories) {
		return this.accommodationReviewHistoryRepository.saveAll(accommodationReviewHistories);
	}

	/*
	 * AccommodationReviewAbbreviation
	 */
	public List<AccommodationReviewAbbreviation> saveAllAccommodationReviewAbbreviation(List<AccommodationReviewAbbreviation> countingInfoList) {
		return this.accommodationReviewAbbreviationRepository.saveAll(countingInfoList);
	}

	/*
	 * AccommodationReviewAbbreviationHistory
	 */
	public AccommodationReviewAbbreviationHistory saveAccommdoationReviewHistory(AccommodationReviewAbbreviationHistory accommodationReviewAbbreviationHistory) {
		return this.accommodationReviewAbbreviationHistoryRepository.save(accommodationReviewAbbreviationHistory);
	}

	public boolean isExistAccommodationReviewAbbreviationHistoryMatchingLatestStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status) {
		return accommodationReviewAbbreviationHistoryRepository.isExistAccommodationReviewAbbreviationHistoryMatchingLatestStatus(user, accommodation, status);
	}

	public AccommodationReviewAbbreviationHistory getLatestAccommodationReviewAbbreviationHistory(Users user, Accommodation accommodation) {
		return accommodationReviewAbbreviationHistoryRepository.findLatestAccommodationReviewAbbreviationHistory(user, accommodation)
			.orElseThrow(() -> {
				throw new NotFoundException("not found AccommodationReviewHistory, user(id : " + user.getId() + " , accommodation(id: " + accommodation.getId() + ")");
			});
	}
}
