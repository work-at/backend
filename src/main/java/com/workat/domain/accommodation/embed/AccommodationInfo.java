package com.workat.domain.accommodation.embed;

import com.workat.domain.tag.AccommodationInfoTag;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AccommodationInfo {

	@ElementCollection
	@CollectionTable(
		name = "accommodation_infos",
		joinColumns = @JoinColumn(name = "accommodation_id")
	)
	@Enumerated(EnumType.STRING)
	private List<AccommodationInfoTag> tags;

	public void add(AccommodationInfoTag tag) {
		tags.add(tag);
	}

	public void remove(AccommodationInfoTag tag) {
		tags.remove(tag);
	}

	public static AccommodationInfo of(List<AccommodationInfoTag> infoTags) {
		return new AccommodationInfo(infoTags);
	}
}
