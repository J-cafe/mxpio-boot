# 模块开发指南

> 本文说明如何基于 mxpio-boot 框架新增一个业务模块。

---

## 目录结构

新增的模块可以放在两个位置：

| 位置 | 说明 |
|------|------|
| `mxpio-framework/` | 框架核心模块（如 `mxpio-common`、`mxpio-jpa`） |
| `mxpio-boot-modules/` | 业务集成模块（如 `mxpio-dingtalk`、`mxpio-email`） |

## 完整步骤

### 步骤 1 — 创建模块目录

```
mxpio-boot-modules/
└── mxpio-report/                       # 报表模块示例
    ├── pom.xml
    └── src/
        └── main/
            ├── java/com/mxpioframework/report/
            │   ├── ReportConfiguration.java
            │   ├── controller/
            │   │   └── ReportController.java
            │   ├── service/
            │   │   ├── ReportService.java
            │   │   └── impl/
            │   │       └── ReportServiceImpl.java
            │   ├── entity/
            │   │   └── ReportDef.java
            │   └── provider/
            │       └── ReportDataProvider.java
            └── resources/
                └── data.sql            # 初始化数据（可选）
```

### 步骤 2 — 编写 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-boot-parent</artifactId>
        <version>4.0.0-beta.1</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>
    <artifactId>mxpio-report</artifactId>
    <name>mxpio-report</name>
    <description>报表管理模块</description>

    <dependencies>
        <dependency>
            <groupId>com.mxpio</groupId>
            <artifactId>mxpio-common</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mxpio</groupId>
            <artifactId>mxpio-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mxpio</groupId>
            <artifactId>mxpio-security</artifactId>
        </dependency>
        <!-- 可选的 Web 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

### 步骤 3 — 创建配置类

```java
package com.mxpioframework.report;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigurationPackage
@ComponentScan
public class ReportConfiguration {

}
```

### 步骤 4 — 注册到 AutoConfiguration.imports

在模块的 `src/main/resources/META-INF/spring/` 下创建：

```
org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

文件内容：

```
com.mxpioframework.report.ReportConfiguration
```

> 或者统一在 `mxpio-autoconfigure` 中注册，适合框架核心模块。

### 步骤 5 — 添加到聚合 POM

编辑对应层级的聚合 POM，在 `<modules>` 中添加。例如，业务集成模块放在 `mxpio-boot-modules/pom.xml`：

```xml
<module>mxpio-report</module>
```

> 框架核心模块添加到 `mxpio-framework/pom.xml`，应用模块添加到 `mxpio-boot-app/pom.xml`。

### 步骤 6 — 注册到 BOM（可选）

如果其他模块需要依赖新模块，在 `mxpio-dependencies/pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-report</artifactId>
    <version>${mxpio.version}</version>
</dependency>
```

## 模块结构惯例

### 实体类

继承 `MxpioEntity`（如果使用统一基类）：

```java
@Entity
@Table(name = "mb_report_def")
public class ReportDef extends MxpioEntity {
    // 字段使用下划线后缀：name_, code_, ...
}
```

### Controller 类

```java
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private Linq linq;

    @GetMapping("/list")
    public Result<List<ReportDef>> list() {
        return Result.ok(linq.from(ReportDef.class).list());
    }
}
```

### 自动初始化 SQL

在 `src/main/resources/` 下添加 `data.sql`，应用启动时会自动执行：

```sql
INSERT INTO mb_report_def(id_, name_, type_, enabled_)
VALUES ('1', '月度报表', 'MONTHLY', 1);
```

## 初始化数据规范

应用配置 `spring.sql.init.mode=always` 时，启动自动执行：

- `schema-{platform}.sql` — 建表 DDL
- `data-{platform}.sql` — 初始化数据 DML

各模块的 `data.sql` 通过 classpath 扫描自动合并执行。
