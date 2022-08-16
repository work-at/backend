package com.workat.domain.user.job;

public enum DepartmentType {

	ENGINEER("개발"),

	DESIGNER("디자이너"),

	PRODUCT("기획 / PM"),

	MARKETING("마케팅"),

	OFFICE("사무직"),

	EXPERT("전문직"),

	FREE("프리랜서");

	private String type;

	DepartmentType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static DepartmentType of(String department) {
		for (DepartmentType departmentType : DepartmentType.values()) {
			if (departmentType.name().equals(department)) {
				return departmentType;
			}
		}

		return null;
	}
}
