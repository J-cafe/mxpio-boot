# mxpio-common — 通用工具模块

## 概述

`mxpio-common` 是 mxpio-boot 框架的基础通用模块，提供全局常量定义、Spring 工具类、反射工具、以及模块注册机制等核心基础设施。所有其他模块都直接或间接依赖此模块。

### 功能特性

- 通用状态码常量（`CommonConstant`）
- Spring Bean 获取工具（`SpringUtil`）
- Bean 反射工具（`BeanReflectionUtils`）
- 模块注册机制（`ModuleVO`）

---

## Maven 依赖

由 BOM 统一管理版本，无需指定 `<version>`。

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-common</artifactId>
</dependency>
```

确保项目中已导入 mxpio-boot BOM：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.mxpio</groupId>
            <artifactId>mxpio-boot</artifactId>
            <version>${mxpio.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## CommonConstant — 通用常量

`CommonConstant` 定义了框架内部使用的一组 HTTP 状态码常量，用于统一 Controller 层返回结果的状态标识。

| 常量名              | 值      | 说明                   |
|---------------------|---------|------------------------|
| `HTTP_OK`           | `200`   | 请求成功               |
| `HTTP_SERVER_ERROR` | `500`   | 服务器内部错误         |
| `HTTP_NO_AUTHZ_401` | `401`   | 未授权（未登录）       |
| `HTTP_NO_AUTHZ_403` | `403`   | 已登录但无权限         |
| `HTTP_NO_AUTHZ_40101` | `40101` | Token 过期或无效       |

### 使用示例

```java
import com.mxpio.common.constant.CommonConstant;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.code = CommonConstant.HTTP_OK;
        r.data = data;
        r.message = "成功";
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.code = CommonConstant.HTTP_SERVER_ERROR;
        r.message = msg;
        return r;
    }
}
```

---

## SpringUtil — Spring Bean 获取工具

`SpringUtil` 实现 `ApplicationContextAware` 接口，持有 Spring 应用上下文，允许在非 Spring 管理的类中获取 Bean。

### 核心方法

| 方法                               | 说明                        |
|------------------------------------|-----------------------------|
| `getBean(String name)`             | 按名称获取 Bean             |
| `getBean(Class<T> clazz)`          | 按类型获取 Bean             |
| `getBean(String name, Class<T> clazz)` | 按名称和类型获取 Bean   |
| `getApplicationContext()`          | 获取 ApplicationContext     |

### 使用示例

```java
import com.mxpio.common.utils.SpringUtil;

// 在普通 POJO 或工具类中获取 Service Bean
public class MyTask {
    public void execute() {
        UserService userService = SpringUtil.getBean(UserService.class);
        List<User> users = userService.findAll();
        // ...
    }
}
```

> **提示**：使用 `SpringUtil.getBean()` 时，确保目标 Bean 已被 Spring 容器管理（标注了 `@Component`, `@Service`, `@Repository` 等注解）。

---

## BeanReflectionUtils — Bean 反射工具

提供基于反射的 Bean 属性操作工具，用于动态获取/设置属性值，支持嵌套属性访问。

### 使用示例

```java
import com.mxpio.common.utils.BeanReflectionUtils;

User user = new User();
user.setUsername("admin");

// 获取属性值
Object username = BeanReflectionUtils.getPropertyValue(user, "username");
// 返回: "admin"

// 设置属性值
BeanReflectionUtils.setPropertyValue(user, "nickname", "管理员");

// 判断是否声明了指定属性
boolean hasField = BeanReflectionUtils.hasProperty(User.class, "email");
```

---

## ModuleVO — 模块注册机制

`ModuleVO` 是模块注册的核心模型，用于在框架启动时自动发现和注册各功能模块的信息。框架会自动收集所有声明的模块，便于统一管理和前端展示。

### 模块注册方式

在模块的 `@Configuration` 类中声明 `ModuleVO` Bean：

```java
import com.mxpio.common.vo.ModuleVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyModuleConfig {

    @Bean
    public ModuleVO myModule() {
        ModuleVO module = new ModuleVO();
        module.setName("my-module");
        module.setTitle("我的模块");
        module.setDescription("提供 XXX 功能的扩展模块");
        module.setVersion("1.0.0");
        module.setAuthor("mxpio");
        return module;
    }
}
```

### ModuleVO 属性说明

| 属性          | 类型     | 说明         |
|---------------|----------|--------------|
| `name`        | String   | 模块标识名称 |
| `title`       | String   | 模块显示标题 |
| `description` | String   | 模块描述信息 |
| `version`     | String   | 模块版本号   |
| `author`      | String   | 模块作者     |

---

## 配置文件

| 配置项                          | 默认值  | 说明               |
|---------------------------------|---------|--------------------|
| `mxpio.common.xxx`（模块自有配置） | —       | 模块相关自定义配置 |

> `mxpio-common` 本身无强制配置项，但作为基础模块通常通过 `application.yml` 统一管理框架配置。

---

## 依赖关系

```
mxpio-common
├── spring-boot-starter (核心)
├── spring-boot-starter-json (JSON 处理)
└── 其他基础库
```

其他 mxpio 模块均依赖 `mxpio-common`：

```
mxpio-jpa → mxpio-common
mxpio-security → mxpio-jpa → mxpio-common
mxpio-quartz → mxpio-jpa → mxpio-common
mxpio-cache → mxpio-common
```
