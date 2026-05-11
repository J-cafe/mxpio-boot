# mxpio-log — 操作日志

> `mxpio-log` 模块基于 `bizlog-sdk` 提供声明式业务操作日志记录，
> 支持数据库和 Elasticsearch 两种存储方式。

---

## 模块说明

操作日志用于记录系统中的关键业务操作（增删改查），满足审计和追溯需求。通过 `@LogRecord` 注解声明在 Service 方法上即可自动记录。

## Maven 依赖

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-log</artifactId>
</dependency>
```

## 配置说明

模块通过 `LogConfiguration` 自动启用：

```java
@Configuration
@AutoConfigurationPackage
@ComponentScan
@EnableAsync
@EnableLogRecord(tenant="com.mxpio")
public class LogConfiguration {
}
```

核心注解 `@EnableLogRecord` 启用了 bizlog-sdk 的日志记录功能，`tenant="com.mxpio"` 定义了租户标识。

## 核心 API

### StorageProvider 接口

定义日志的存储行为：

```java
public interface StorageProvider {
    String getProviderName();
    void saveLog(LogRecord logRecord);
    Page<LogVO> listPage(Pageable pageable, LogParam param);
}
```

**内置实现：**

| 实现类 | 说明 |
|--------|------|
| `DatabaseStorageProvider` | 默认实现，日志存储到 `DBMxpioLog` 数据库表 |
| `ElasticsearchStorageProvider` | 日志存储到 Elasticsearch |

### 日志查询

通过 `MxpioLogController` 的 API：

```
GET /log/listPage?page=0&size=10
GET /log/listPage?page=0&size=10&type=USER&bizNo=BIZ-001
```

`LogVO` 包含字段：操作人、操作类型、业务主键、操作内容、操作时间等。

### 日志服务

- `LogRecordService` — 日志记录服务
- `MxpioLogService` — 日志查询服务
- `MxpioDatabaseLogServiceImpl` — 数据库存储实现

## 使用示例

### 1. 在 Service 中添加日志注解

```java
@Service
public class OrderService {

    @LogRecord(
        success = "{{#operator}} 创建了订单 {{#orderNo}}",
        type = "ORDER",
        bizNo = "{{#orderNo}}"
    )
    public Order createOrder(String orderNo, String operator) {
        // 业务逻辑
        return order;
    }

    @LogRecord(
        success = "{{#operator}} 更新了订单 {{#orderNo}}，变更金额为 {{#amount}}",
        type = "ORDER",
        bizNo = "{{#orderNo}}"
    )
    public void updateAmount(String orderNo, BigDecimal amount, String operator) {
        // 业务逻辑
    }
}
```

### 2. SpEL 表达式

`@LogRecord` 的 `success`、`type`、`bizNo` 等属性支持 Spring SpEL 表达式：

| 表达式 | 说明 |
|--------|------|
| `{{#operator}}` | 方法参数 `operator` 的值 |
| `{{#orderNo}}` | 方法参数 `orderNo` 的值 |
| `{{#result.id}}` | 返回值 `result` 的 `id` 属性 |
| `{{#_errorMsg}}` | 错误信息（失败时） |

### 3. 查询操作日志

```http
# 分页查询所有日志
GET /log/listPage?page=0&size=20

# 按类型筛选
GET /log/listPage?page=0&size=20&type=ORDER

# 按业务主键查询
GET /log/listPage?page=0&size=20&bizNo=ORDER-2025-001
```

## 扩展：自定义存储

实现 `StorageProvider` 接口：

```java
@Component
public class MongoStorageProvider implements StorageProvider {

    @Override
    public String getProviderName() {
        return "MongoDB";
    }

    @Override
    public void saveLog(LogRecord logRecord) {
        // 保存到 MongoDB
    }

    @Override
    public Page<LogVO> listPage(Pageable pageable, LogParam param) {
        // 从 MongoDB 查询
        return null;
    }
}
```

> 日志记录默认异步执行（`@EnableAsync`），不会影响主业务流程性能。
