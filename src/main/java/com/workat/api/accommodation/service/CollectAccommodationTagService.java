package com.workat.api.accommodation.service;

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
		List<TagDto> list = AccommodationReviewTag.ALL
			.stream()
			.map(TagDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}

	public TagsResponse collectInfoTags() {
		List<TagDto> list = AccommodationInfoTag.ALL
			.stream()
			.map(TagDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}
}
