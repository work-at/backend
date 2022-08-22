package com.workat.domain.tour.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.workat.domain.accommodation.RegionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "monthly")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyRegionVisitor {

	@Id
	private String id;

	private double count;

	private MonthlyRegionVisitor(String id, double count) {
		this.id = id;
		this.count = count;
	}

	public static MonthlyRegionVisitor of(RegionType regionCode, double count) {
		return new MonthlyRegionVisitor(regionCode.getCode(), count);
	}

}
