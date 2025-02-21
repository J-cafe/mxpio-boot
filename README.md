# Mxpio-Boot

简体中文 | [English](https://gitee.com/i_mxpio/mxpio-boot/blob/master/README.en.md)

![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/J-cafe/mxpio-boot?include_prereleases)
![GitHub](https://img.shields.io/github/license/J-cafe/mxpio-boot)
![GitHub top language](https://img.shields.io/github/languages/top/J-cafe/mxpio-boot)
![OSCS Status](https://www.oscs1024.com/platform/badge/J-cafe/mxpio-boot.svg?size=small)
## 1.简介
>MxpIO Boot基于Spring Boot研发的低代码快速开发框架,整合和封装了企业常用的功能及组件，开箱即用。MxpIO Boot采用宽泛的[MIT](https://gitee.com/i_mxpio/mxpio-boot/blob/master/LICENSE)开源协议，完全开源。

**Vue2前端地址：**[Mxpio-Boot-Antd-Vue](https://gitee.com/i_mxpio/mxpio-boot-antd-vue)

## 2.在线文档

**在线文档：**[mxpio-boot](https://mxpio.com/)

## 3.后端技术栈

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

### 3.1模块

> MxpIO-Boot采用Spring Boot风格的模块管理。通过mxpio-boot-base-autoconfigure模块管理各个模块的自动装配，具体模块之间的继承关系如下：

```
mxpio-boot-parent
├─mxpio-boot-base-autoconfigure // 自动装配模块
├─mxpio-boot-base-common // 公共模块
├─mxpio-boot-base-cache // 缓存接口模块
├─mxpio-boot-base-jpa // JPA模块
├─mxpio-boot-base-log // 日志模块
├─mxpio-boot-base-expression // 表达式模块
├─mxpio-boot-base-security // 权鉴模块
├─mxpio-boot-base-system // 系统管理模块
├─mxpio-boot-base-excel // Excel模块
├─mxpio-boot-base-camunda // 工作流模块
├─mxpio-boot-base-quartz // Job管理模块
├─mxpio-boot-base-message // 消息通知模块
├─mxpio-boot-base-multitenant // 多租户
├─mxpio-boot-base-dbconsole // 云数据库
├─mxpio-boot-module-cache-redis // 缓存Redis实现
├─mxpio-boot-module-cache-caffeine // 缓存Caffeine实现（与Redis二选一）
└─mxpio-boot-webapp // 业务项目


```

## 4.中间件技术栈

* 关系型数据库：Mysql/Oracle/Mssql/Postgresql等
* 缓存中间件：Redis

## 5.前端技术栈

> 前端项目基于优秀的Vue开源项目[Vue-Antd-Admin](https://gitee.com/iczer/vue-antd-admin)开发。

* Vue
* Vuex
* Vue-Cli
* Vue-Router
* Vue-i18n
* Ant-Design-Vue
* Vxe-Table
* Axios
* Viser

## 6.演示环境

筹备中.

## 7.快速开始

### 7.1运行示例项目

示例代码库：[https://gitee.com/i_mxpio/mxpio-boot-example](https://gitee.com/i_mxpio/mxpio-boot-example)


检出代码

```bash
git clone https://gitee.com/i_mxpio/mxpio-boot-example.git
```
修改配置文件resources/application-dev.yml

```yaml
server:
  # 服务端口号
  port: 9005
  tomcat:
    max-swallow-size: -1
  servlet:
    # 服务跟路径
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
  # 数据库配置
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    sql-script-encoding: UTF-8
    continue-on-error: true
    initialization-mode: ALWAYS
  # redis配置
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
编译打包

```bash
cd mxpio-boot-example
mvn clean package spring-boot:repackage

```
启动项目

```bash
java -jar mxpio-boot-example\target\mxpio-boot-example-1.0.12-beta.11.jar
```

### 7.2新建Maven项目运行

修改pom.xml文件

```xml
<!-- 继承mxpio-boot-parent -->
<parent>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-parent</artifactId>
	<version>1.0.12-beta.11</version>
</parent>
```

```xml
<!-- 添加模块依赖 -->
<dependencies>
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
</dependencies>
```

修改配置文件resources/application-dev.yml的数据库信息和服务端口等信息

```yaml
server:
  # 服务端口号
  port: 9005
  tomcat:
    max-swallow-size: -1
  servlet:
    # 服务跟路径
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
  # 数据库配置
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    sql-script-encoding: UTF-8
    continue-on-error: true
    initialization-mode: ALWAYS
  # redis配置
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

编译打包

```bash
cd mxpio-boot-example
mvn clean package spring-boot:repackage

```
启动项目

```bash
java -jar target\mxpio-boot-example-1.0.12-beta.11.jar
```

### 7.3源码运行

检出代码

```bash
git clone https://gitee.com/i_mxpio/mxpio-boot.git
```

修改配置文件mxpio-boot-webapp/resources/application-dev.yml的数据库信息和服务端口等信息

```yaml
server:
  # 服务端口号
  port: 9005
  tomcat:
    max-swallow-size: -1
  servlet:
    # 服务跟路径
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
  # 数据库配置
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    sql-script-encoding: UTF-8
    continue-on-error: true
    initialization-mode: ALWAYS
  # redis配置
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

编译打包

```bash
cd mxpio-boot
mvn clean package spring-boot:repackage

```
启动项目

```bash
java -jar mxpio-boot-webapp\target\mxpio-boot-webapp-1.0.12-beta.11.jar
```


## 8.使用登记

以下是一些正在使用此框架的知名公司：

- **河南人才集团** - [人才集团](http://www.hn-talent.com/)
- **郑州卓臻信息技术有限公司** - [数字卓臻](https://www.datazhzh.com/)
- **山东禾美网络科技有限公司** - [山东禾美](http://www.unidbg.cn/)

我们非常感谢这些公司对项目的支持和贡献！如果你的公司也在使用此框架，并且愿意被列在这里，请通过[Gitee Issue](https://gitee.com/i_mxpio/mxpio-boot/issues/IAMNUX)与我们联系。

## 9.截图

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E7%99%BB%E5%BD%95.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E8%8F%9C%E5%8D%95.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E8%A7%92%E8%89%B2.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E9%83%A8%E9%97%A8.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E5%AF%BC%E5%85%A5%E7%AE%A1%E7%90%86.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E5%AF%BC%E5%87%BA%E7%AE%A1%E7%90%86.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E5%AD%97%E5%85%B8.png)

![加载失败](https://gitee.com/i_mxpio/mxpio-boot/raw/master/screenshots/%E4%BB%BB%E5%8A%A1%E8%B0%83%E5%BA%A6.png)

## 10.其他

感谢[JetBrains](https://www.jetbrains.com/)提供的IDE授权