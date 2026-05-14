# 配置体系说明

## 概述

mxpio-boot 采用 **模块级默认配置 + 应用级覆盖配置** 的机制。每个模块在自己的 `src/main/resources/mxpio/mxpio.properties` 中声明默认配置，应用工程（如 `mxpio-boot-example`）可以在自己的 `mxpio.properties` 中覆盖这些默认值。

**加载优先级**（高 → 低）：
1. `mxpio-boot-example/src/main/resources/mxpio/mxpio.properties` — **应用级覆盖**，最高优先级
2. 各模块 `src/main/resources/mxpio/mxpio.properties` — **模块级默认值**
3. `application.yml` / `application-mysql.yml` — Spring Boot 标准配置

> 所有以 `mxpio.` 为前缀的配置项均可通过 Spring Boot 的 `application.yml` 覆盖（优先级高于 properties）。

## 配置详解

### 1. mxpio-common — 全局应用信息

**位置**: `mxpio-framework/mxpio-common/src/main/resources/mxpio/mxpio.properties`

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.swagger.title` | MxpIO Boot Framework | Swagger 文档标题 |
| `mxpio.swagger.desc` | MxpIO-Boot Framework API | Swagger 文档描述 |
| `mxpio.swagger.version` | 1.0.12-beta.11 | API 版本号 |
| `mxpio.swagger.author` | MxpIO | 作者 |
| `mxpio.swagger.email` | i@mxpio.com | 联系邮箱 |
| `mxpio.swagger.homepage` | http://www.mxpio.com | 项目主页 |
| `mxpio.swagger.license` | MIT License | 开源协议 |
| `mxpio.swagger.licenseUrl` | https://gitee.com/i_mxpio/... | 协议地址 |
| `app.system.desc` | MxpIO-Boot | 系统描述 |
| `app.name` | MxpIO-Boot | 应用名称 |
| `app.system.abbr` | MxpIO-Boot | 系统缩写 |
| `app.user.company` | 数字卓粤 | 公司名称 |

### 2. mxpio-security — 安全与权限体系

**位置**: `mxpio-framework/mxpio-security/src/main/resources/mxpio/mxpio.properties`

**应用信息**:

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.appName` | MxpIO Development Framework | 应用名称，显示在登录页 |
| `mxpio.loginImageBg` | static/images/login-bg.jpg | 登录页背景图片 |
| `mxpio.loginPath` | login | 登录页面地址 |
| `mxpio.loginSuccessPage` | main | 登录成功后跳转页面 |
| `mxpio.logoutPath` | logout | 退出地址 |
| `mxpio.personCenter` | PersonalCenter | 个人中心页面地址 |
| `mxpio.logo` | /resstatic/logo.png | 系统 Logo 图片路径 |
| `mxpio.autoCreateIfUserIsEmpty` | true | 首次启动用户为空时自动创建 admin/123456 |
| `mxpio.JSEncrypt.privateKey` | null | RSA 私钥（用于前端密码加密） |

**匿名访问设置**:

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.systemAnonymous` | /static/**,... | 系统级匿名访问 URL 路径 |
| `mxpio.customAnonymous` | (空) | 用户自定义匿名访问 URL 路径 |

**验证码**:

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.captcha.open` | true | 是否开启登录验证码 |
| `mxpio.captcha.border` | no | 验证码图片边框 |
| `mxpio.captcha.textproducer.font.color` | blue | 验证码字体颜色 |
| `mxpio.captcha.image.width` | 125 | 验证码图片宽度 |
| `mxpio.captcha.image.height` | 45 | 验证码图片高度 |
| `mxpio.captcha.textproducer.font.size` | 43 | 验证码字体大小 |
| `mxpio.captcha.textproducer.char.length` | 4 | 验证码字符长度 |

**密码策略**:

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.password.expiredswitch` | on | 密码过期策略开关（on/off） |
| `mxpio.password.expireddays` | 180 | 密码过期天数 |
| `mxpio.password.strategy` | DIGANDLETTERCASE,... | 密码强度策略 |
| `mxpio.resetPassword` | 123456 | 重置后的默认密码 |

> **密码强度策略选项**：`DIGANDLETTER`（数字+字母）、`DIGANDLETTERCASE`（数字+大小写字母）、`CONTINUOUS`（禁止连续字符）、`MINLENGTH`（最小长度）、`MINLENGTH12`（最小12位）

**Token 与登录**:

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.token.time` | 1800000 | Token 有效期（毫秒，默认30分钟） |
| `mxpio.refresh.token.time` | 7200000 | Refresh Token 有效期（毫秒，默认2小时） |
| `mxpio.loginerror.locktime` | 300 | 登录失败锁定时间（秒） |
| `mxpio.passwordCheckPolicy` | LocalPasswordCheckPolicy | 密码校验策略实现类 |

**权限控制**:

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.componentPermissionFlat` | false | 组件权限配置扁平化 |
| `mxpio.componentPermissionSupportType` | (空) | 支持的组件类型，多个用逗号隔开 |
| `mxpio.allowIfAllAbstainDecisions` | true | 未分配组件是否允许无权限访问 |

### 3. mxpio-system — 系统管理

**位置**: `mxpio-framework/mxpio-system/src/main/resources/mxpio/mxpio.properties`

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.generate.path` | D:\\workplace\\ | 代码生成输出路径 |
| `mxpio.systemResourceLocation` | ./resourcefile | 系统资源文件存储位置 |
| `mxpio.systemResourceMappingPath` | /resstatic/ | 资源文件 URL 映射路径 |

### 4. mxpio-multitenant — 多租户

**位置**: `mxpio-framework/mxpio-multitenant/src/main/resources/mxpio/mxpio.properties`

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.multitenant.masterDatabaseName` | mboot | 主数据库名称 |
| `mxpio.multitenant.disableRegister` | false | 是否禁用租户注册 |
| `mxpio.multitenant.organizationIdRegExp` | ^[a-zA-Z]\w* | 组织ID格式校验正则 |
| `mxpio.multitenant.databaseNameSuffix` | (空) | 租户数据库名称后缀 |
| `mxpio.multitenant.databaseNamePrefix` | mboot_ | 租户数据库名称前缀 |
| `mxpio.multitenant.resourceScript` | classpath*:data.sql | 租户初始化 SQL 脚本路径 |
| `mxpio.multitenant.packagesToScan` | com.mxpioframework.**.entity | 实体类扫描包路径 |
| `mxpio.multitenant.customPackagesToScan` | (空) | 自定义实体扫描包路径 |

### 5. mxpio-excel — Excel 导入导出

**位置**: `mxpio-framework/mxpio-excel/src/main/resources/mxpio/mxpio.properties`

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.excel.exporter.location` | (空) | 导出文件存储位置 |
| `mxpio.excel.exporter.cacheSize` | 500 | 导出缓存大小 |
| `mxpio.excel.exporter.cvs.delimiter` | , | CSV 分隔符 |
| `mxpio.excel.exporter.cvs.cellWrapSymbol` | (空) | CSV 单元格包裹符号 |
| `mxpio.excel.exporter.cvs.charset` | UTF-8 | CSV 字符编码 |
| `mxpio.excel.exporter.extension.fileType` | (空) | 导出文件类型扩展 |
| `mxpio.excel.swfviewer.pdfToSwf` | (空) | PDF 转 SWF 工具路径 |
| `mxpio.excel.swfviewer.xpdfPath` | (空) | Xpdf 工具路径 |

### 6. mxpio-filestorage — 文件存储

**位置**: `mxpio-framework/mxpio-filestorage/src/main/resources/mxpio/mxpio.properties`

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.fileSystemStorageLocation` | fileStorage/ | 本地文件系统存储根路径 |
| `mxpio.enableDatabaseLocalCache` | true | 是否启用数据库本地缓存 |
| `mxpio.databaseStorageLocalCacheLocation` | cache/ | 数据库本地缓存位置 |
| `mxpio.defaultFileStorageProviderType` | FileSystem | 默认存储提供商类型（FileSystem/MinIO） |

### 7. mxpio-dingtalk — 钉钉集成

**位置**: `mxpio-boot-modules/mxpio-dingtalk/src/main/resources/mxpio/mxpio.properties`

| 配置项 | 说明 |
|--------|------|
| `dingtalk.appKey` | 钉钉应用 AppKey |
| `dingtalk.appSecret` | 钉钉应用 AppSecret |
| `dingtalk.agentId` | 钉钉应用 AgentId |

> ⚠️ 这些配置项默认注释掉，需根据实际应用信息填写。

### 8. mxpio-boot-example — 应用级覆盖

**位置**: `examples/mxpio-boot-example/src/main/resources/mxpio/mxpio.properties`

此文件中的配置会**覆盖**各模块的默认值。默认内容：

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `mxpio.password.expiredswitch` | off | ⚡ 覆盖 security 模块默认值：关闭密码过期 |
| `mxpio.password.expireddays` | 180 | 密码过期天数 |
| `mxpio.password.strategy` | DIGANDLETTER,... | 密码强度策略 |
| `mxpio.captcha.open` | false | ⚡ 覆盖 security 模块默认值：关闭验证码 |

### 9. 其他模块

以下模块的 `mxpio.properties` 文件留空（无默认配置），预留以备后续扩展：

- **mxpio-quartz** (mxpio-framework/mxpio-quartz)
- **mxpio-log** (mxpio-framework/mxpio-log)
- **mxpio-camunda** (mxpio-framework/mxpio-camunda)
- **mxpio-wechat** (mxpio-boot-modules/mxpio-wechat)

---

## 自定义配置示例

在 `application.yml` 中覆盖 mxpio 配置：

```yaml
mxpio:
  appName: 我的铝业生产管理系统
  swagger:
    title: 生产管理 API
    desc: 生产管理系统接口文档
  security:
    # 验证码配置
    captcha:
      open: true
  password:
    expiredswitch: on
    expireddays: 90
  filestorage:
    fileSystemStorageLocation: /data/fileStorage/
```

或者在 `mxpio-boot-example/src/main/resources/mxpio/mxpio.properties` 中覆盖：

```properties
mxpio.appName=我的铝业生产管理系统
mxpio.swagger.title=生产管理 API
mxpio.password.expiredswitch=on
```
