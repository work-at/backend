package com.workat.api.accommodation.dto;

import java.util.HashSet;

import com.workat.domain.tag.dto.TagDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccommodationDetailDto {

	private long id;

	@ApiModelProperty(name = "name", notes = "Accommodation 이름", example = "그랜드워커힐서울")
	private String name;

	@ApiModelProperty(name = "imgUrl", notes = "디테일 뷰 배너 이미지", example = "https://lh5.googleusercontent.com/p/AF1QipOKO_7oTuHLK31fOjhqp13KompnHRxgi_2_oOVT=w253-h168-k-no")
	private String imgUrl;

	@ApiModelProperty(name = "price", notes = "1박 평균 예상 가격", example = "235000")
	private long price;

	@ApiModelProperty(name = "phone", notes = "Accommodation 전화번호", example = "1670-0005")
	private String phone;

	@ApiModelProperty(name = "roadAddressName", notes = "도로명 주소", example = "서울 광진구 워커힐로 177 그랜드워커힐 서울")
	private String roadAddressName;

	@ApiModelProperty(name = "placeUrl", notes = "카카오 맵 url", example = "https://kko.to/jhJN8vUzS")
	private String placeUrl;

	@ApiModelProperty(name = "relatedUrl", notes = "관련 url", example = "https://www.walkerhill.com/grandwalkerhillseoul")
	private String relatedUrl;

	@ApiModelProperty(name = "infoTags", notes = "Accommodation info tags", example = "[ { \"name\": \"NEAR_CITY\", \"content\": \"도시 인근\" }, { \"name\": \"WORKSPACE\", \"content\": \"숙소 내 업무 공간\" }, { \"name\": \"SHARED_WORKSPACE\", \"content\": \"공용업무공간\" } ]")
	private HashSet<TagDto> infoTags;

	public AccommodationDetailDto(long id, String name, String imgUrl, long price, String phone,
		String roadAddressName, String placeUrl, String relatedUrl,
		HashSet<TagDto> infoTags) {
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.price = price;
		this.phone = phone;
		this.roadAddressName = roadAddressName;
		this.placeUrl = placeUrl;
		this.relatedUrl = relatedUrl;
		this.infoTags = infoTags;
	}
}
