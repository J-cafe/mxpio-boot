package com.mxpioframework.filestorage.provider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.filestorage.entity.MxpioFileInfo;

public interface FileStorageProvider {

	String getType();

	String put(InputStream inputStream,String fileName,long fileSize,String contentType) throws IOException;

	String put(MultipartFile file) throws IllegalStateException, IOException;

	InputStream getInputStream(String relativePath) throws IOException;

	String getAbsolutePath(String relativePath) throws FileNotFoundException;

	int remove(MxpioFileInfo mxpioFileInfo);
}
