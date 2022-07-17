package com.workat.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReadUtils {

	public static List<List<String>> readCSV(File file) {
		List<List<String>> result = new ArrayList<>();
		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((line = br.readLine()) != null) {
				result.add(Arrays.asList(line.split(",")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
