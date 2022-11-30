package com.mxpioframework.filestorage.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.filestorage.entity.MxpioFileInfo;

public interface FileStorageService {
	
	public static final String BEAN_ID = "mxpio.fileStorageService";

	MxpioFileInfo put(InputStream inputStream, String filename,long fileSize,String contentType) throws IOException;

	MxpioFileInfo put(MultipartFile file, String filename) throws IllegalStateException, IOException;

	MxpioFileInfo put(String fileStroageType, InputStream inputStream, String filename,long fileSize,String contentType) throws IOException;

	MxpioFileInfo put(String fileStroageType, MultipartFile file, String filename)
			throws IllegalStateException, IOException;

	MxpioFileInfo get(String fileNo) throws FileNotFoundException;

	MxpioFileInfo get(Integer fileId) throws FileNotFoundException;

	String getAbsolutePath(String fileNo) throws FileNotFoundException;

	InputStream getInputStream(String fileNo) throws FileNotFoundException;

	InputStream getInputStream(MxpioFileInfo cokeFileInfo) throws FileNotFoundException;
}
