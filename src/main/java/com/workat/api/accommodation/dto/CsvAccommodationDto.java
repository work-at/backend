package com.workat.api.accommodation.dto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.workat.domain.accommodation.RegionType;
import com.workat.domain.tag.info.AccommodationInfoTag;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CsvAccommodationDto {

	private RegionType regionType;

	private String name;

	private String placeUrl;

	private String roadAddress;

	private String phone;

	private String price;

	private String sharedWorkspace;

	private String workspace;

	private String nearForest;

	private String nearSea;

	private String nearAttraction;

	private String nearCity;

	private String relatedUrl;

	private String thumbnailImgUrl;

	private String imgUrl;

	public static CsvAccommodationDto of(RegionType regionType, List<String> line) {
		CsvAccommodationDto dto = new CsvAccommodationDto();

		int index = 0;
		Field[] fields = dto.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				if (field.getName().equals("regionType")) {
					field.set(dto, regionType);
					continue;
				}
				field.set(dto, line.get(index++));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(CsvAccommodationDto.class.getSimpleName() + " factory exception : " + e);
			}
		}

		return dto;
	}

	public List<AccommodationInfoTag> getInfoTags() {
		ArrayList<AccommodationInfoTag> infos = new ArrayList<>();

		if (checkAccommodationInfo(this.getSharedWorkspace())) {
			infos.add(AccommodationInfoTag.SHARED_WORKSPACE);
		}

		if (checkAccommodationInfo(this.getWorkspace())) {
			infos.add(AccommodationInfoTag.WORKSPACE);
		}

		if (checkAccommodationInfo(this.getNearForest())) {
			infos.add(AccommodationInfoTag.NEAR_FOREST);
		}

		if (checkAccommodationInfo(this.getNearSea())) {
			infos.add(AccommodationInfoTag.NEAR_SEA);
		}

		if (checkAccommodationInfo(this.getNearAttraction())) {
			infos.add(AccommodationInfoTag.NEAR_ATTRACTION);
		}

		if (checkAccommodationInfo(this.getNearCity())) {
			infos.add(AccommodationInfoTag.NEAR_CITY);
		}

		return infos;
	}

	private boolean checkAccommodationInfo(String target) {
		return target.equals("O") || target.equals("o") || target.equals("0");
	}
}
