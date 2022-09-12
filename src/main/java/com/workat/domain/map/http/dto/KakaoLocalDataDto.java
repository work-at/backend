package com.workat.domain.map.http.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoLocalDataDto {

	@EqualsAndHashCode.Include
	private String id;

	private String phone;

	private String placeName;

	private String placeUrl;

	private String categoryName;

	private String x;

	private String y;

	private String addressName;

	private String roadAddressName;

	private String thumbnailImageUrl;

	private String fullImageUrl;

	@Builder
	public KakaoLocalDataDto(String id, String phone, String placeName, String placeUrl, String categoryName, String x, String y, String addressName, String roadAddressName) {
		this.id = id;
		this.phone = phone;
		this.placeName = placeName;
		this.placeUrl = placeUrl;
		this.categoryName = categoryName;
		this.x = x;
		this.y = y;
		this.addressName = addressName;
		this.roadAddressName = roadAddressName;
	}

	public void updateImages(String[] imageUrls) {
		this.thumbnailImageUrl = imageUrls[0];
		this.fullImageUrl = imageUrls[1];
	}

}
