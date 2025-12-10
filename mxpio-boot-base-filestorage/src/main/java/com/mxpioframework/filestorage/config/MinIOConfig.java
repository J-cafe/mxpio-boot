package com.mxpioframework.filestorage.config;

import com.mxpioframework.filestorage.provider.impl.MinIOStorageProvider;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnClass(name = {"io.minio.MinioClient"})
@ConditionalOnProperty(name="mxpio.defaultFileStorageProviderType",havingValue = MinIOStorageProvider.ProviderType)
public class MinIOConfig {

    private static final Logger logger = LoggerFactory.getLogger(MinIOConfig.class);

    @Value("${mxpio.minio.endpoint}")
    private String endpoint;

    @Value("${mxpio.minio.accessKey}")
    private String accessKey;

    @Value("${mxpio.minio.secretKey}")
    private String secretKey;

    @Value("${mxpio.minio.bucketName}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        //init bucket
        try{
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        }
        catch (Exception e){
            logger.error("MinIO bucket初始化失败");
        }

        return minioClient;
    }
}
