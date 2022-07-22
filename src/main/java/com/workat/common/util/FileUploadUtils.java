package com.workat.common.util;

import java.io.File;
import java.io.IOException;

import org.springframework.util.FileCopyUtils;

public class FileUploadUtils {

	public static String fileUpload(String uploadPath, String fileName, byte[] fileData) throws IOException {
		File image = new File(uploadPath + File.separator + fileName + ".png");
		FileCopyUtils.copy(fileData, image);

		return image.getPath();
	}
}
