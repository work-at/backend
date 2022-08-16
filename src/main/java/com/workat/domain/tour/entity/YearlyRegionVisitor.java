package com.workat.domain.tour.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.workat.domain.tour.RegionCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "yearly")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YearlyRegionVisitor {

	@Id
	private String id;

	private double count;

	private YearlyRegionVisitor(String id, double count) {
		this.id = id;
		this.count = count;
	}

	public static YearlyRegionVisitor of(RegionCode regionCode, double count) {
		return new YearlyRegionVisitor(regionCode.getCode(), count);
	}

}
