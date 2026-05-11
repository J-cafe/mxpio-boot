package com.mxpioframework.dbconsole.utils;

import java.io.File;
import java.io.IOException;

public final class FileUtils {
	private FileUtils() {
	}

	public static void clearDirectory(File dir) throws IOException {
		if (!dir.isDirectory()) {
			return;
		}

		for (File subFile : dir.listFiles()) {
			if (subFile.isFile()) {
				if (!subFile.delete()) {
					throw new IOException("Can not delete \""
							+ subFile.getAbsolutePath() + "\".");
				}
			} else if (subFile.isDirectory()) {
				removeDirectory(subFile);
			}
		}
	}

	public static void removeDirectory(File dir) throws IOException {
		if (!dir.isDirectory()) {
			return;
		}

		clearDirectory(dir);
		if (!dir.delete()) {
			throw new IOException("Can not delete \"" + dir.getAbsolutePath()
					+ "\".");
		}
	}
}
