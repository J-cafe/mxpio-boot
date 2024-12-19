package com.mxpioframework.filestorage.provider.impl;

import com.mxpioframework.filestorage.entity.MxpioFileInfo;
import com.mxpioframework.filestorage.provider.FileStorageProvider;
import io.minio.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@ConditionalOnClass(name = {"io.minio.MinioClient"})
public class MinIOStorageProvider implements FileStorageProvider {

    private static final Logger logger = LoggerFactory.getLogger(MinIOStorageProvider.class);

    @Value("${mxpio.minio.bucketName}")
    private String bucketName;

    @Autowired(required = false)
    private MinioClient minioClient;

    public static final String ProviderType = "MinIO";

    @Override
    public String getType() {
        return ProviderType;
    }

    @Override
    public String put(InputStream inputStream, String fileName,long fileSize,String contentType) throws IOException {
        String minioFileName = randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String objectName = DateFormatUtils.format(new Date(), "yyyy/MM/dd") + "/" + minioFileName;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(inputStream, fileSize, -1).contentType(contentType).build();
            // 文件名称相同会覆盖
            minioClient.putObject(objectArgs);
            return bucketName+"/"+objectName;
        } catch (Exception e) {
            logger.error(">>>>>>>>>上传失败:{}",e.getMessage(),e);
        }
        return null;
    }

    @Override
    public String put(MultipartFile file) throws IllegalStateException, IOException {
        return put(file.getInputStream(), Objects.requireNonNull(file.getOriginalFilename()), file.getSize(),file.getContentType());
    }

    @Override
    public InputStream getInputStream(String relativePath) throws IOException {
        String bucketName = StringUtils.substring(relativePath,0,StringUtils.indexOf(relativePath,"/"));
        String objectPath = StringUtils.substring(relativePath,StringUtils.indexOf(relativePath,"/")+1);
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectPath).build();
        try{
            return minioClient.getObject(objectArgs);
        }
        catch (Exception e){
            logger.error(">>>>>>>>>读取失败:{}",e.getMessage(),e);
        }
        return null;
    }

    @Override
    public String getAbsolutePath(String relativePath) throws FileNotFoundException {
        return relativePath;
    }

    private static String randomUUID(){
        String uuid = UUID.randomUUID().toString();
        return StringUtils.join(uuid.split("-"));
    }

	@Override
	public int remove(MxpioFileInfo mxpioFileInfo) {
        String bucketName = StringUtils.substring(mxpioFileInfo.getRelativePath(),0,StringUtils.indexOf(mxpioFileInfo.getRelativePath(),"/"));
        String objectPath = StringUtils.substring(mxpioFileInfo.getRelativePath(),StringUtils.indexOf(mxpioFileInfo.getRelativePath(),"/")+1);
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(objectPath).build();
        try{
            minioClient.removeObject(removeObjectArgs);
            return 1;
        }
        catch (Exception e){
            logger.error(">>>>>>>>>删除失败:{}",e.getMessage(),e);
        }
		return 0;
	}
}
