package com.workat.api.map.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocationImageGenerator {

	private static final String DEFAULT_IMAGE_PATH =
		File.separator + "locations" + File.separator;

	@Value("${server.host:}")
	private String serverHost;

	public String[] generateImageUrl(String placeCategory) {
		int random = (int)((Math.random() * (placeCategory.contains("burger") ? 2 : 5) + 1));
		String basePath = generatedImagePath(placeCategory);
		String numberString = String.format("%02d", random);
		String thumbnail = basePath + "thumb_" + numberString + ".png";
		String full = basePath + numberString + ".png";
		return new String[] {thumbnail, full};
	}

	private String generatedImagePath(String placeCategory) {
		if (placeCategory.contains("술집")) {
			return DEFAULT_IMAGE_PATH + "bar_";
		}

		if (placeCategory.contains("디저트")) {
			return DEFAULT_IMAGE_PATH + "dessert_";
		}

		if (placeCategory.contains("카페")) {
			return DEFAULT_IMAGE_PATH + "cafe_";
		}

		if (placeCategory.contains("한식")) {
			return DEFAULT_IMAGE_PATH + "korea_";
		}

		if (placeCategory.contains("일식")) {
			return DEFAULT_IMAGE_PATH + "japan_";
		}

		if (placeCategory.contains("중식")) {
			return DEFAULT_IMAGE_PATH + "china_";
		}

		if (placeCategory.contains("햄버거")) {
			//버거는 2개
			return DEFAULT_IMAGE_PATH + "burger_";
		}

		if (placeCategory.contains("양식")) {
			return DEFAULT_IMAGE_PATH + "western_";
		}

		if (placeCategory.contains("분식")) {
			return DEFAULT_IMAGE_PATH + "snack_";
		}

		if (placeCategory.contains("아시아음식")) {
			return DEFAULT_IMAGE_PATH + "asia_";
		}

		if (placeCategory.contains("도시락")) {
			return DEFAULT_IMAGE_PATH + "packed_";
		}

		return DEFAULT_IMAGE_PATH + "etc_";
	}
}
