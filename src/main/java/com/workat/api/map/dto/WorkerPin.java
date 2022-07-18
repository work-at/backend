package com.workat.api.map.dto;

import org.springframework.data.geo.Point;

public interface WorkerPin {

	Long getUserId();

	Point getLocation();
}
