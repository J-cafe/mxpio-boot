package com.mxpioframework.filestorage.provider.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.filestorage.entity.MxpioBlob;
import com.mxpioframework.filestorage.entity.MxpioFileInfo;
import com.mxpioframework.filestorage.provider.FileStorageProvider;
import com.mxpioframework.filestorage.service.MxpioBlobService;

@Service
public class DatabaseStorageProvider implements FileStorageProvider {

	public static final String ProviderType = "Database";

	@Value("${mxpio.enableDatabaseLocalCache:true}")
	private boolean enableLocalFileCache;

	@Value("${mxpio.databaseStorageLocalCacheLocation:}")
	private String databaseCachedfileSystemStorageLocation;

	@Resource
	private MxpioBlobService mxpioBlobService;

	@Override
	public String getType() {
		return ProviderType;
	}

	@Override
	public String put(InputStream inputStream, String fileName, long fileSize, String contentType) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(inputStream, baos);
		MxpioBlob mxpioBlob = mxpioBlobService.put(baos.toByteArray());
		return mxpioBlob.getId();
	}

	@Override
	public String put(MultipartFile file) throws IllegalStateException, IOException {
		return put(file.getInputStream(), file.getOriginalFilename(), file.getSize(), file.getContentType());
	}

	public String getAbsolutePath(String relativePath) throws FileNotFoundException {
		if (!enableLocalFileCache) {
			return null;
		}

		String path = null;
		File file = null;
		path = DigestUtils.md5Hex(relativePath);
		path = rebuildString(path, File.separator, 8, 4, 4, 4);
		file = new File(databaseCachedfileSystemStorageLocation + path);
		if (file.exists()) {
			return file.getAbsolutePath();
		}

		MxpioBlob mxpioBlob = mxpioBlobService.get(relativePath);
		if (mxpioBlob != null) {
			byte[] data = mxpioBlob.getData();
			InputStream inputStream = new ByteArrayInputStream(data);
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			try {
				IOUtils.copy(inputStream, fileOutputStream);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(fileOutputStream);
			}
		} else {
			throw new FileNotFoundException("MxpioBlob Record not found " + relativePath);
		}
		return file.getAbsolutePath();
	}

	@Override
	public InputStream getInputStream(String relativePath) throws IOException {
		InputStream inputStream = null;
		String absolutePath = getAbsolutePath(relativePath);
		if (absolutePath != null) {
			inputStream = Files.newInputStream(Paths.get(absolutePath));
		} else {
			MxpioBlob mxpioBlob = mxpioBlobService.get(relativePath);
			if (mxpioBlob != null) {
				byte[] data = mxpioBlob.getData();
				inputStream = new ByteArrayInputStream(data);
			}

		}
		return inputStream;
	}

	static String rebuildString(String target, String separator, int... lengths) {
		int length = target.length();
		int builderLength = length + lengths.length * separator.length();
		StringBuilder builder = new StringBuilder(builderLength);
		int start = 0;
		for (int i : lengths) {
			builder.append(target, start, start + i).append(separator);
			start += i;
		}
		builder.append(target.substring(start));
		return builder.toString();
	}

	@Override
	public int remove(MxpioFileInfo mxpioFileInfo) {
		return 0;
	}

}
