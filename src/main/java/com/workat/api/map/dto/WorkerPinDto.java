package com.workat.api.map.dto;

import org.springframework.data.geo.Point;

public interface WorkerPinDto {

	String getUserId();

	Point getLocation();
}
