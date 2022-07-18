package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerPinDto {

	long id;
	double longitude;
	double latitude;

	private WorkerPinDto(long id, double longitude, double latitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public static WorkerPinDto of(WorkerPin workerPin) {
		return new WorkerPinDto(workerPin.getUserId(), workerPin.getLocation().getX(), workerPin.getLocation().getY());
	}
}
