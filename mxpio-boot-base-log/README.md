# mxpio-boot-base-log

#### 介紹
本模块对mzt-biz-log日志框架进行了封装，主要针对业务日志，使用@LogRecord注解进行记录，
参考：https://github.com/mouzt/mzt-biz-log
#### 使用說明

1. 本模块支持数据库与elasticsearch两种方式记录日志。
2. 如果采用数据库方式，直接在POM文件中引入本模块即可，模块默认采用数据库方式。
```   
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
```
3. 如果需要采用es，首先在POM文件中引入本模块
```   
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
```
然后在POM文件中引入spring-boot-starter-data-elasticsearch
```   
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
```
最后，在mxpio.properties中配置采用es模式
```
mxpio.log.provider=es
```
当然，别忘了在yml文件中配置elasticsearch相关配置
```
spring:
  elasticsearch:
    rest:
      uris: "https://search.example.com:9200"
      read-timeout: "10s"
      username: "user"
      password: "secret"
```

注意：由于spring-boot-starter-data-elasticsearch版本与elasticsearch版本兼容性问题，
本模块需使用elasticsearch7.12.1版本， 如需其他版本的elasticsearch,请自行调整依赖。