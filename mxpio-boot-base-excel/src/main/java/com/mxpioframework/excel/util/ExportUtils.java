package com.mxpioframework.excel.util;

import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

public class ExportUtils {

	public static String getFileStorePath() {
		String fileLocation = "D://";
		if (StringUtils.isNotEmpty(fileLocation)) {
			return fileLocation.endsWith(File.separator) ? fileLocation : fileLocation + File.separator;
		} else {
			fileLocation = System.getProperty("java.io.tmpdir");
			if (!fileLocation.endsWith(File.separator)) {
				fileLocation += File.separator;
			}
			File file = new File(fileLocation + "mxpio-export-temp");
			if (!file.exists()) {
				file.mkdirs();
			}
			return fileLocation + "mxpio-export-temp"+ File.separator;
		}
	}

	public static File getFile(String id, String name) {
		if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(name)) {
			String fullName = id + "_" + name;
			return new File(ExportUtils.getFileStorePath(), fullName);
		}
		return null;
	}

	public static SimpleDateFormat getSimpleDateFormat() {
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf;
	}

}
