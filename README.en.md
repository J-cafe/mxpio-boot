# Mxpio-Boot

[简体中文](README.md) | English

![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/J-cafe/mxpio-boot?include_prereleases)
![GitHub](https://img.shields.io/github/license/J-cafe/mxpio-boot)
![GitHub top language](https://img.shields.io/github/languages/top/J-cafe/mxpio-boot)
![OSCS Status](https://www.oscs1024.com/platform/badge/J-cafe/mxpio-boot.svg?size=small)
## 1.Introduction
>MxpIO Boot is a low code rapid development framework developed based on Spring Boot, which integrates and encapsulates commonly used enterprise functions and components, making it ready to use out of the box. MxpIO Boot adopts a broad [MIT]（ https://gitee.com/i_mxpio/mxpio-boot/blob/master/LICENSE ）Open source protocol, completely open source.

**Vue2 Front framework repository：**[Mxpio-Boot-Antd-Vue](https://gitee.com/i_mxpio/mxpio-boot-antd-vue)

## 2.Documents

**Online Documents：**[mxpio-boot](https://doc.datazhzh.com/)

## 3.Backend Technology Stack

* Spring Boot 2.5.14
* Spring Data Jpa
* Spring Data Redis
* Spring Security
* Spring Cache
* Alibaba Druid
* SpringDoc
* Jwt
* Poi
* Camunda
* Quartz

### 3.1 Modules

> MxpIO Boot adopts Spring Boot style module management. Manage the automatic assembly of various modules through the mxpio boot base auto configure module, and the specific inheritance relationships between modules are as follows:

```
mxpio-boot-parent
├─mxpio-boot-base-autoconfigure
├─mxpio-boot-base-common
├─mxpio-boot-base-cache
├─mxpio-boot-base-jpa
├─mxpio-boot-base-log
├─mxpio-boot-base-expression
├─mxpio-boot-base-security
├─mxpio-boot-base-system
├─mxpio-boot-base-excel
├─mxpio-boot-base-camunda
├─mxpio-boot-base-quartz
├─mxpio-boot-base-message
├─mxpio-boot-base-multitenant
├─mxpio-boot-base-dbconsole
├─mxpio-boot-module-cache-redis
├─mxpio-boot-module-cache-caffeine
└─mxpio-boot-webapp


```

## 4.Middleware Technology Stack

* Relational DB：Mysql/Oracle/Mssql/Postgresql
* Cache DB：Redis

## 5.Front Technology Stack

> The front-end project is based on the excellent Vue open source project[Vue-Antd-Admin](https://gitee.com/iczer/vue-antd-admin)Development.

* Vue
* Vuex
* Vue-Cli
* Vue-Router
* Vue-i18n
* Ant-Design-Vue
* Vxe-Table
* Axios
* Viser

## 6.Demo

Coming soon.

## 7.Quick Start

### 7.1 Run through example project

Example Code Repository:[https://gitee.com/i_mxpio/mxpio-boot-example](https://gitee.com/i_mxpio/mxpio-boot-example)


Check out

```bash
git clone https://gitee.com/i_mxpio/mxpio-boot-example.git
```
edit file:resources/application-dev.yml

```yaml
server:
  # Server
  port: 9005
  tomcat:
    max-swallow-size: -1
  servlet:
    # Path
    context-path: 
spring:
  servlet:
     multipart:
        max-file-size: 10MB
        max-request-size: 10MB
  jpa:
    open-in-view: false
    showSql: true
    hibernate:
      ddl-auto: update
  # db
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    sql-script-encoding: UTF-8
    continue-on-error: true
    initialization-mode: ALWAYS
  # redis
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    timeout:
    pool:
      maxActive: 8
      maxWait: -1
      maxIdle: 8
      minIdle: 0
...

```
Compile

```bash
cd mxpio-boot-example
mvn clean package spring-boot:repackage

```
Start

```bash
java -jar mxpio-boot-example\target\mxpio-boot-example-1.0.12-beta.10.jar
```

### 7.2 Run as a new maven project

edit pom.xml file

```xml
<!-- extends mxpio-boot-parent -->
<parent>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-parent</artifactId>
	<version>1.0.12-beta.10</version>
</parent>
```

```xml
<!-- dependencies -->
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-autoconfigure</artifactId>
</dependency>
<!-- <dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-module-cache-redis</artifactId>
</dependency> -->
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-module-cache-caffeine</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-security</artifactId>
</dependency>
<!-- <dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-multitenant</artifactId>
</dependency> -->
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-excel</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-quartz</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-camunda</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-filestorage</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-log</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-expression</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-system</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-dbconsole</artifactId>
</dependency>
```

edit file:resources/application-dev.yml

```yaml
server:
  # Server
  port: 9005
  tomcat:
    max-swallow-size: -1
  servlet:
    # Path
    context-path: 
spring:
  servlet:
     multipart:
        max-file-size: 10MB
        max-request-size: 10MB
  jpa:
    open-in-view: false
    showSql: true
    hibernate:
      ddl-auto: update
  # DB
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    sql-script-encoding: UTF-8
    continue-on-error: true
    initialization-mode: ALWAYS
  # redis
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    timeout:
    pool:
      maxActive: 8
      maxWait: -1
      maxIdle: 8
      minIdle: 0
...

```

Compile

```bash
cd mxpio-boot-example
mvn clean package spring-boot:repackage

```
Start

```bash
java -jar target\mxpio-boot-example-1.0.12-beta.10.jar
```

### 7.3 Run from source code

Check out

```bash
git clone https://gitee.com/i_mxpio/mxpio-boot.git
```

edit file:mxpio-boot-webapp/resources/application-dev.yml

```yaml
server:
  # Server
  port: 9005
  tomcat:
    max-swallow-size: -1
  servlet:
    # Path
    context-path: 
spring:
  servlet:
     multipart:
        max-file-size: 10MB
        max-request-size: 10MB
  jpa:
    open-in-view: false
    showSql: true
    hibernate:
      ddl-auto: update
  # DB
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    sql-script-encoding: UTF-8
    continue-on-error: true
    initialization-mode: ALWAYS
  # redis
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    timeout:
    pool:
      maxActive: 8
      maxWait: -1
      maxIdle: 8
      minIdle: 0
...

```

Compile

```bash
cd mxpio-boot
mvn clean package spring-boot:repackage

```
Start

```bash
java -jar mxpio-boot-webapp\target\mxpio-boot-webapp-1.0.12-beta.10.jar
```


## 8.User registration

Here are some well-known companies currently using this framework:

- **Henan Talent Group** - [人才集团](http://www.hn-talent.com/)
- **Zhengzhou Zhuozhen Information Technology Co., Ltd** - [数字卓臻](https://www.datazhzh.com/)

We are very grateful for the support and contribution of these companies to the project! If your company is also using this framework and willing to be listed here, please use [Gitee Issue]（ https://gitee.com/i_mxpio/mxpio-boot/issues/IAMNUX ）Contact us.

## 9.Screenshot

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E7%99%BB%E5%BD%95.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E8%8F%9C%E5%8D%95.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E8%A7%92%E8%89%B2.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E9%83%A8%E9%97%A8.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E5%AF%BC%E5%85%A5%E7%AE%A1%E7%90%86.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E5%AF%BC%E5%87%BA%E7%AE%A1%E7%90%86.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E5%AD%97%E5%85%B8.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E4%BB%BB%E5%8A%A1%E8%B0%83%E5%BA%A6.png)
