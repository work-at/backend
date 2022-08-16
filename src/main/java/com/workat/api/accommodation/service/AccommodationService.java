package com.workat.api.accommodation.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.workat.api.accommodation.dto.AccommodationDetailDto;
import com.workat.api.accommodation.dto.AccommodationDto;
import com.workat.api.accommodation.dto.AccommodationReviewDto;
import com.workat.api.accommodation.dto.response.AccommodationResponse;
import com.workat.api.accommodation.dto.response.AccommodationsResponse;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.dto.TagDto;
import com.workat.domain.tag.dto.TagWithCountDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccommodationService {

	public AccommodationsResponse getAccommodations(
		RegionType region,
		AccommodationInfoTag infoTagName,
		AccommodationReviewTag accommodationReviewTag,
		int pageNumber,
		int pageSize
	) {

		return AccommodationsResponse.of(
			Arrays.asList(AccommodationDto.of(1L, "그랜드워커힐서울", 235000L,
					"https://lh5.googleusercontent.com/p/AF1QipOKO_7oTuHLK31fOjhqp13KompnHRxgi_2_oOVT=w253-h168-k-no",
					Arrays.asList(
						TagDto.of(AccommodationReviewTag.FOCUS),
						TagDto.of(AccommodationReviewTag.SERVE_MEAL),
						TagDto.of(AccommodationReviewTag.WIFI)
					)
				),
				AccommodationDto.of(2L, "씨마크호텔", 135000L,
					"https://lh5.googleusercontent.com/proxy/pxIAn34FA3bpLmWfDBKZe6uTiFdb7JrocuP7tzcLTTWcINIqCKLsuADqZW65VteN0bZ28rWStDjNwGjBNhr4_V8KHjBW7aWhNkORBP3Jw9UFmeqive-omWDVIUh5HwVj29V9wi_7iOoUKcvCG6XduQ6Bl2MYyQ=w253-h184-k-no",
					Arrays.asList(
						TagDto.of(AccommodationReviewTag.WIFI),
						TagDto.of(AccommodationReviewTag.POWER),
						TagDto.of(AccommodationReviewTag.SERVE_MEAL)
					)
				)
			), pageNumber, pageSize, 150
		);
	}

	public AccommodationResponse getAccommodation(long accommodationId) {

		return AccommodationResponse.of(
			AccommodationDetailDto.builder()
				.id(accommodationId)
				.name("그랜드워커힐서울")
				.imgUrl(
					"https://lh5.googleusercontent.com/p/AF1QipOKO_7oTuHLK31fOjhqp13KompnHRxgi_2_oOVT=w253-h168-k-no")
				.price(235000L)
				.phone("1670-0005")
				.roadAddressName("서울 광진구 워커힐로 177 그랜드워커힐 서울")
				.placeUrl("https://kko.to/jhJN8vUzS")
				.relatedUrl("https://www.walkerhill.com/grandwalkerhillseoul")
				.infoTags(Arrays.asList(
					TagDto.of(AccommodationInfoTag.NEAR_CITY),
					TagDto.of(AccommodationInfoTag.WORKSPACE),
					TagDto.of(AccommodationInfoTag.SHARED_WORKSPACE)
				))
				.build(),
			AccommodationReviewDto.of(
				Arrays.asList(
					TagWithCountDto.of(TagDto.of(AccommodationReviewTag.SERVE_MEAL), 90),
					TagWithCountDto.of(TagDto.of(AccommodationReviewTag.FOCUS), 75),
					TagWithCountDto.of(TagDto.of(AccommodationReviewTag.WIFI), 60),
					TagWithCountDto.of(TagDto.of(AccommodationReviewTag.POWER), 55),
					TagWithCountDto.of(TagDto.of(AccommodationReviewTag.QUIET), 40),
					TagWithCountDto.of(TagDto.of(AccommodationReviewTag.SEAT), 30)
				),
				false,
				1
			)
		);
	}
}
