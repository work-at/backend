package com.workat.domain.user.job;

public enum DepartmentType {

	IT_ENGINEER("IT 엔지니어 및 보안"),

	DESIGNER("디자이너"),

	IT_PRODUCT_MANAGER("IT 기획 및 매니지먼트"),

	MARKETING("마케팅, 홍보, 광고"),

	MERCHANDISER("상품 기획, MD"),

	BUSINESS_MANAGER("경영전략, 관리"),

	HUMAN_RESOURCE("인사, 노무, 법무"),

	SUPPLY_MANAGER("구매, 물류, SCM"),

	SALES("영업, 해외영업"),

	ACCOUNTANT("회계, 재무, 세무"),

	TRADE("유통, 무역"),

	FINANCE("금융"),

	OTHERS("기타");

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
