package com.mxpioframework.filestorage.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.filestorage.entity.MxpioFileInfo;
import com.mxpioframework.filestorage.provider.FileStorageProvider;
import com.mxpioframework.filestorage.service.FileStorageService;
import com.mxpioframework.jpa.JpaUtil;

@Service(FileStorageService.BEAN_ID)
public class FileStorageServiceImpl implements FileStorageService {

	private Map<String, FileStorageProvider> fileStorageProviderMap = new HashMap<String, FileStorageProvider>();

	@Override
	public MxpioFileInfo put(InputStream inputStream, String filename,long fileSize,String contentType) throws IOException {
		return put(defaultFileStorageProviderType, inputStream, filename, fileSize,contentType);

	}

	@Override
	public MxpioFileInfo put(MultipartFile file, String filename) throws IllegalStateException, IOException {
		return put(defaultFileStorageProviderType, file, filename);
	}

	@Override
	@Transactional(readOnly = false)
	public MxpioFileInfo put(String fileStorageType, InputStream inputStream, String filename,long fileSize,String contentType) throws IOException {
		String relativePath = getFileStorageProvider(fileStorageType).put(inputStream,filename,fileSize,contentType);
		return saveFile(fileStorageType, relativePath, filename);
	}

	public MxpioFileInfo saveFile(String fileStorageType, String relativePath, String filename) {
		MxpioFileInfo fileInfo = new MxpioFileInfo();
		fileInfo.setFileName(filename);
		fileInfo.setCreateTime(new Date());
		fileInfo.setFileNo(UUID.randomUUID().toString());
		fileInfo.setFileStorageType(fileStorageType);
		fileInfo.setRelativePath(relativePath);
		JpaUtil.save(fileInfo);
		return fileInfo;
	}

	@Override
	public MxpioFileInfo put(String fileStorageType, MultipartFile file, String filename)
			throws IllegalStateException, IOException {
		String relativePath = getFileStorageProvider(fileStorageType).put(file);
		return saveFile(fileStorageType, relativePath, filename);

	}

	@Override
	@Transactional(readOnly = true)
	public MxpioFileInfo get(String fileNo) throws FileNotFoundException {
		MxpioFileInfo mxpioFileInfo = JpaUtil.linq(MxpioFileInfo.class).equal("fileNo", fileNo).findOne();

		return buildAbsolutePath(mxpioFileInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public MxpioFileInfo get(Integer fileId) throws FileNotFoundException {
		MxpioFileInfo mxpioFileInfo = JpaUtil.linq(MxpioFileInfo.class).idEqual(fileId).findOne();
		return buildAbsolutePath(mxpioFileInfo);
	}

	MxpioFileInfo buildAbsolutePath(MxpioFileInfo mxpioFileInfo) throws FileNotFoundException {
		if (mxpioFileInfo != null) {
			String absolutePath = getFileStorageProvider(mxpioFileInfo.getFileStorageType())
					.getAbsolutePath(mxpioFileInfo.getRelativePath());
			mxpioFileInfo.setAbsolutePath(absolutePath);
		}
		return mxpioFileInfo;
	}

	FileStorageProvider getFileStorageProvider(String fileStorageType) {
		FileStorageProvider fileStorageProvider = null;
		if (StringUtils.isEmpty(fileStorageType)) {
			fileStorageType = defaultFileStorageProviderType;
		}
		fileStorageProvider = fileStorageProviderMap.get(fileStorageType);
		if (fileStorageProvider == null) {
			throw new RuntimeException("unknow FileStrorageType " + fileStorageType);
		}
		return fileStorageProvider;
	}

	@Value("${mxpio.defaultFileStorageProviderType:}")
	private String defaultFileStorageProviderType;

	@Autowired
	public void setFileStorageProviderMap(Collection<FileStorageProvider> fileStorageProviders) {
		if (fileStorageProviders == null) {
			return;
		}

		for (FileStorageProvider fileStorageProvider : fileStorageProviders) {
			this.fileStorageProviderMap.put(fileStorageProvider.getType(), fileStorageProvider);
		}
	}

	@Override
	public InputStream getInputStream(String fileNo) throws FileNotFoundException {
		MxpioFileInfo fileInfo = get(fileNo);
		if (fileInfo == null) {
			return null;
		}
		return getInputStream(fileInfo);
	}

	@Override
	public InputStream getInputStream(MxpioFileInfo mxpioFileInfo) throws FileNotFoundException {
		return getFileStorageProvider(mxpioFileInfo.getFileStorageType())
				.getInputStream(mxpioFileInfo.getRelativePath());
	}

	@Override
	public String getAbsolutePath(String fileNo) throws FileNotFoundException {
		MxpioFileInfo mxpioFileInfo = get(fileNo);
		return mxpioFileInfo != null ? mxpioFileInfo.getAbsolutePath() : null;
	}

	@Override
	@Transactional(readOnly = false)
	public int remove(String fileNo) throws FileNotFoundException {
		MxpioFileInfo mxpioFileInfo = get(fileNo);
		
		int count = getFileStorageProvider(defaultFileStorageProviderType).remove(mxpioFileInfo);
		if(count > 0){
			JpaUtil.delete(mxpioFileInfo);
		}
		return count;
	}

}
