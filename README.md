# mxpio-boot（孵化中）
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/J-cafe/mxpio-boot?include_prereleases)
![GitHub](https://img.shields.io/github/license/J-cafe/mxpio-boot)
![GitHub top language](https://img.shields.io/github/languages/top/J-cafe/mxpio-boot)
## 1.简介
>MxpIO-Boot基于spring-boot研发的开发框架。参考spring boot项目结构构建，目前处于孵化中。

## 2.后端技术栈

* Spring Boot 2.4.3
* Spring Data Jpa
* Spring Data Redis
* Spring Security
* Spring Cache
* Alibaba Fastjson
* Alibaba Druid
* Springfox Swagger2
* Jwt
* Lombok
* Easy-Captcha

### 2.1模块继承关系

> MxpIO-Boot采用Spring Boot风格的模块管理。通过mxpio-boot-base-autoconfigure模块管理各个模块的自动装配，具体模块之间的继承关系如下：

```
mxpio-boot-parent
├─mxpio-boot-base-autoconfigure // 自动装配模块
├─mxpio-boot-base-common // 公共模块
├─mxpio-boot-base-cache // 缓存接口模块
├─mxpio-boot-base-jpa // JPA模块
├─mxpio-boot-base-security // 权鉴模块
├─mxpio-boot-base-system // 系统管理模块
├─mxpio-boot-base-ui // UI设计模块
├─mxpio-boot-module-cache-redis // 缓存Redis实现
└─mxpio-boot-webapp // 业务项目


```

## 3.中间件技术栈

* 关系型数据库：Mysql/Oracle/Mssql/Postgresql等
* 缓存中间件：Redis

## 4.前端技术栈（孵化中）

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

检出代码

```
git clone https://gitee.com/i_mxpio/mxpio-boot.git
```

启动项目

```
cd mxpio-boot
mvn clean package spring-boot:repackage
java -jar mxpio-boot-webapp\target\mxpio-boot-webapp-1.0.0.jar
```

## 7.开发文档

- 在线文档：筹备中...

- Linq JPA说明： [mxpio-boot-base-jpa](https://gitee.com/i_mxpio/mxpio-boot/tree/master/mxpio-boot-base-jpa/README.md)

- 权限说明： [mxpio-boot-base-security](https://gitee.com/i_mxpio/mxpio-boot/blob/master/mxpio-boot-base-security/README.md)

## 8.开发计划

筹备中.

