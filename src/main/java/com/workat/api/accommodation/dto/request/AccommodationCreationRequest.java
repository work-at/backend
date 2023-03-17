package com.workat.api.accommodation.dto.request;

import com.workat.domain.accommodation.RegionType;
import com.workat.domain.tag.AccommodationInfoTag;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccommodationCreationRequest {

	private RegionType regionType;

	private String name;

	private String imgUrl;

	private String thumbnailUrl;

	private long price;

	private String phone;

	private String roadAddressName;

	private String placeUrl;

	private String relateUrl;

	private List<AccommodationInfoTag> infoTagList;
}
