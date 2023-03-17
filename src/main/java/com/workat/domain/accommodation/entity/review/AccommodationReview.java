package com.workat.domain.accommodation.entity.review;

import com.workat.domain.BaseEntity;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.abbreviation.AccommodationReviewAbbreviation;
import com.workat.domain.tag.AccommodationReviewTag;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccommodationReview extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "accommodation_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Accommodation accommodation;

	private int reviewedUserCnt = 0;

	@OrderBy(value = "cnt desc")
	@JoinColumn(name = "accommodation_review_id")
	@OneToMany(fetch = FetchType.LAZY)
	private List<AccommodationReviewAbbreviation> countingInfoList = new ArrayList<>();

	public void increaseUserCnt() {
		this.reviewedUserCnt++;
	}

	public void decreaseUserCnt() {
		if (this.reviewedUserCnt > 0) {
			this.reviewedUserCnt--;
		} else {
			throw new RuntimeException("reviewedUserCnt must over 0");
		}
	}

	public List<AccommodationReviewAbbreviation> getTopRank(int number) {
		int maxLength = Math.min(countingInfoList.size(), number);
		return this.countingInfoList.subList(0, maxLength);
	}

	public void addReviews(List<AccommodationReviewTag> tags) {
		tags.forEach(tag -> {
			AccommodationReviewAbbreviation info = countingInfoList.stream()
				.filter(countingInfo -> countingInfo.getCategory().equals(tag))
				.findFirst()
				.orElse(null);

			if (info == null) {
				info = AccommodationReviewAbbreviation.of(tag);
				countingInfoList.add(info);
			}

			info.increaseCnt();
		});
	}

	public static AccommodationReview of(Accommodation accommodation) {
		return new AccommodationReview(null, accommodation, 0, new ArrayList<>());
	}
}
