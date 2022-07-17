package com.workat.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

class FileReadUtilsTest {

	@Test
	void readCsvTest() {
		//given
		File givenFile = new File(getClass().getResource("/csv/seoul_subway.csv").getPath());

		//when
		List<List<String>> result = FileReadUtils.readCSV(givenFile);

		//then
		assertEquals(288, result.size());
	}
}
