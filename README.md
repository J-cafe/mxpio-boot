# mxpio-boot（孵化中）
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/J-cafe/mxpio-boot?include_prereleases)
![GitHub](https://img.shields.io/github/license/J-cafe/mxpio-boot)
![GitHub top language](https://img.shields.io/github/languages/top/J-cafe/mxpio-boot)
![OSCS Status](https://www.oscs1024.com/platform/badge/J-cafe/mxpio-boot.svg?size=small)
## 1.简介
>MxpIO Boot基于Spring Boot研发的开发框架，目前处于孵化中。MxpIO Boot采用宽泛的[MIT](https://gitee.com/i_mxpio/mxpio-boot/blob/master/LICENSE)开源协议，完全开源。

前端地址：[Mxpio-Boot-Antd-Vue](https://gitee.com/i_mxpio/mxpio-boot-antd-vue)
## 2.后端技术栈

* Spring Boot 2.5.14
* Spring Data Jpa
* Spring Data Redis
* Spring Security
* Spring Cache
* Alibaba Druid
* Springfox Swagger2
* Jwt
* Lombok

### 2.1模块继承关系

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
├─mxpio-boot-base-flowable // 工作流模块
├─mxpio-boot-base-quartz // Job管理模块
├─mxpio-boot-base-ui // UI设计模块
├─mxpio-boot-module-cache-redis // 缓存Redis实现
└─mxpio-boot-webapp // 业务项目


```

## 3.中间件技术栈

* 关系型数据库：Mysql/Oracle/Mssql/Postgresql等
* 缓存中间件：Redis

## 4.前端技术栈（孵化中）

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

## 5.演示环境

筹备中.

## 6.快速开始

### 6.1运行示例项目

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
java -jar mxpio-boot-example\target\mxpio-boot-example-1.0.12-beta.1.jar
```

### 6.2新建Maven项目运行

修改pom.xml文件

```xml
<!-- 继承mxpio-boot-parent -->
<parent>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-parent</artifactId>
	<version>1.0.12-beta.1</version>
</parent>
```

```xml
<!-- 添加模块依赖 -->
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-autoconfigure</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-module-cache-redis</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-security</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-system</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-flowable</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-quartz</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-excel</artifactId>
</dependency>
<dependency>
	<groupId>com.mxpio</groupId>
	<artifactId>mxpio-boot-base-log</artifactId>
</dependency>
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
java -jar target\mxpio-boot-example-1.0.12-beta.1.jar
```

### 6.3源码运行

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
java -jar mxpio-boot-webapp\target\mxpio-boot-webapp-1.0.12-beta.1.jar
```

## 7.开发文档

- 在线文档：筹备中...

- Linq JPA说明： [mxpio-boot-base-jpa](https://gitee.com/i_mxpio/mxpio-boot/tree/master/mxpio-boot-base-jpa/README.md)

- 权限说明： [mxpio-boot-base-security](https://gitee.com/i_mxpio/mxpio-boot/blob/master/mxpio-boot-base-security/README.md)

- 表达式说明： [mxpio-boot-base-expression](https://gitee.com/i_mxpio/mxpio-boot/blob/master/mxpio-boot-base-expression/README.md)

## 8.开发计划


| 模块 | 功能 | 后端 | 前端 |
|:-----:|:----:|:----:|:----:|
| 权限管理 | 用户权限 | 初版已完成 | 初版已完成 |
|       | 角色管理 | 初版已完成 | 初版已完成 |
|       | 菜单管理 | 初版已完成 | 初版已完成 |
|       | 部门管理 | 初版已完成 | 初版已完成 |
|       | 数据权限 | 未开始 | 未开始 |
|       | 组件权限 | 未开始 | 未开始 |
|       | 字段权限 | 未开始 | 未开始 |
| 表达式模块 |       | 初版已完成 | 不涉及 |
| 系统监控 | 性能监控 | 初版已完成 | 初版已完成 |
|       | 日志监控 | 初版进行中 | 未开始 |
| 字典配置 | 字典管理 | 初版已完成 | 初版已完成 |
|       | 字典翻译 | 初版已完成 | 初版已完成 |
|       | 字典缓存 | 未开始 | 不涉及 |
| JPA工具 | JPA工具 | 初版已完成 | 不涉及 |
| Excel | 导入管理 | 初版已完成 | 初版已完成 |
|       | 导出管理 | 初版进行中 | 未开始 |
| 工作流 |       | 初版进行中 | 初版进行中 |
| 任务调度 |       | 初版已完成 | 初版进行中 |
| 报表模块 |       | 未开始 | 未开始 |
| 图表模块 |       | 未开始 | 未开始 |
| 多租户 |       | 未开始 | 未开始 |
| 代码生成 |       | 进行中 | 未开始 |

## 相关开源项目

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue-Antd-Admin](https://gitee.com/iczer/vue-antd-admin)
- [Druid](https://github.com/alibaba/druid)
