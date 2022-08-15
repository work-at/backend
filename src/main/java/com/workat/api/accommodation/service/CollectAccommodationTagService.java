package com.workat.api.accommodation.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.accommodation.dto.response.TagsResponse;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.dto.TagDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CollectAccommodationTagService {

	public TagsResponse collectReviewTags() {
		List<TagDto> list = Arrays.stream(AccommodationReviewTag.values())
			.map(TagDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}

	public TagsResponse collectInfoTags() {
		List<TagDto> list = Arrays.stream(AccommodationInfoTag.values())
			.map(TagDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}
}
