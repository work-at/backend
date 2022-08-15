package com.workat.api.tour.dto;

public enum VisitorDegree {

	POPULAR("완전핫함"),
	IN_BETWEEN("핫플직전"),
	FREE("한산해요");

	private String message;

	VisitorDegree(String message) {
		this.message = message;
	}
}
