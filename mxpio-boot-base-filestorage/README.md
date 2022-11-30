# mxpio-boot-base-filestorage
## 简介
filestorage文件管理模块主要包含文件的上传、存储、下载等的管理。
## 使用方法
#### 标准接口
* /file/upload 文件上传
* /file/download/{fileNo} 文件下载
> 详细接口参数参见SpringDoc文档

#### 文件存储

> 不同的文件存储方式可以通过实现FileStorageProvider接口来扩展。系统默认提供了两种FileStorageProvider的实现。分别是FileSystemStorageProvider和DatabaseStorageProvider。可以通过mxpio/mxpio.properties配置文件设置默认的存储提供者以及储存路径等信息。如果这两种不能满足需求，还可以实现自己的FileStorageProvider。
> 目前已扩展支持MinIO的MinIOStorageProvider。

FileStorageProvider接口

```java

public interface FileStorageProvider {

	String getType();

    String put(InputStream inputStream,String fileName,long fileSize,String contentType) throws IOException;

    String put(MultipartFile file) throws IllegalStateException, IOException;

    InputStream getInputStream(String relativePath) throws FileNotFoundException;

    String getAbsolutePath(String relativePath) throws FileNotFoundException;
}

```

mxpio/mxpio.properties配置文件

```

# 本地文件存储器根路径
mxpio.fileSystemStorageLocation=fileStorage/
# 数据库存储器缓存是否开启
mxpio.enableDatabaseLocalCache=true
# 数据库存储器缓存路径
mxpio.databaseStorageLocalCacheLocation=cache/
# 默认存储器提供者
mxpio.defaultFileStorageProviderType=FileSystem

```

MinIOStorageProvider使用方法

```
1.pom.xml文件中配置依赖

<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
</dependency>

2.mxpio/mxpio.properties配置文件配置MinIO相关属性

#MinIO服务器endpoint
mxpio.minio.endpoint=
#MinIO服务器accessKey
mxpio.minio.accessKey=
#MinIO服务器secretKey
mxpio.minio.secretKey=
#文件要存放的bucket
mxpio.minio.bucketName=

3.配置存储器，两种方法：
  1）修改默认存储器
     mxpio.defaultFileStorageProviderType=MinIO
  2）调用/file/upload接口时fileStorageType参数传值MinIO
```


