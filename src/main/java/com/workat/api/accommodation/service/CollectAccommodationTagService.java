package com.workat.api.accommodation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.accommodation.dto.response.TagsResponse;
import com.workat.domain.tag.info.AccommodationInfoTag;
import com.workat.domain.tag.review.AccommodationReviewTag;
import com.workat.domain.tag.dto.TagDto;
import com.workat.domain.tag.dto.TagInfoDto;
import com.workat.domain.tag.dto.TagContentDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CollectAccommodationTagService {

	public TagsResponse collectReviewTags() {
		List<TagDto> list = AccommodationReviewTag.ALL
			.stream()
			.map(TagContentDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}

	public TagsResponse collectInfoTags() {
		List<TagInfoDto> list = AccommodationInfoTag.ALL
			.stream()
			.map(TagInfoDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}
}
