# 自定义与扩展

> mxpio-boot 框架设计了多个扩展点，开发者可以在不修改框架源码的情况下
> 定制或替换各模块的行为。

---

## 1. 自定义权限提供者

### GrantedAuthorityProvider

实现自定义权限来源，例如从外部系统同步角色：

```java
@Component
public class ExternalRoleAuthorityProvider implements GrantedAuthorityProvider {

    @Override
    public List<GrantedAuthority> provide(String userId) {
        // 从外部系统查询用户角色
        List<String> roles = externalRoleService.getRoles(userId);
        return roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getProviderName() {
        return "ExternalRole";
    }
}
```

### FilterConfigAttributeProvider

自定义 URL 访问控制策略：

```java
@Component
public class CustomFilterConfigAttributeProvider implements FilterConfigAttributeProvider {

    @Override
    public ConfigAttributeCollection getAttributes(String url) {
        // 根据 URL 返回需要的权限
        if (url.startsWith("/api/public")) {
            return null; // 公开接口，无需权限
        }
        return SecurityConfig.createList("ROLE_USER");
    }
}
```

## 2. 自定义数据范围

实现 `DataScapeProvider` 自定义数据权限过滤规则：

```java
@Component
public class CustomDataScapeProvider implements DataScapeProvider {

    @Override
    public String getProviderName() {
        return "CustomDataScope";
    }

    @Override
    public List<String> getAccessibleDataIds(String userId, String resourceCode) {
        // 返回用户可访问的数据 ID 列表
        if ("ORDER".equals(resourceCode)) {
            return orderService.getAccessibleOrderIds(userId);
        }
        return Collections.emptyList();
    }
}
```

## 3. 自定义缓存实现

实现 `CacheProvider` 接口使用其他缓存中间件：

```java
@Component
public class CustomCacheProvider implements CacheProvider {

    // 假设使用本地 ConcurrentHashMap
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public boolean hasKey(String key) {
        return cache.containsKey(key);
    }

    @Override
    public void del(String... keys) {
        for (String key : keys) {
            cache.remove(key);
        }
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public boolean set(String key, Object value) {
        cache.put(key, value);
        return true;
    }

    @Override
    public boolean set(String key, Object value, long time) {
        cache.put(key, value);
        return true;
    }

    @Override
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        cache.put(key, value);
        return true;
    }
}
```

## 4. 自定义日志存储

实现 `StorageProvider` 将操作日志存储到其他系统：

```java
@Component
public class MongoStorageProvider implements StorageProvider {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String getProviderName() {
        return "MongoDB";
    }

    @Override
    public void saveLog(LogRecord logRecord) {
        mongoTemplate.save(logRecord, "operation_logs");
    }

    @Override
    public Page<LogVO> listPage(Pageable pageable, LogParam param) {
        // 从 MongoDB 分页查询
        Query query = new Query();
        // 组装查询条件...
        long count = mongoTemplate.count(query, "operation_logs");
        List<LogVO> logs = mongoTemplate.find(
            query.with(pageable), LogVO.class, "operation_logs");
        return new PageImpl<>(logs, pageable, count);
    }
}
```

## 5. 自定义消息渠道

实现 `MessageChannel` 添加新的消息发送方式（如短信、飞书）：

```java
@Component
public class SmsMessageChannel implements MessageChannel {

    @Override
    public String getChannelCode() {
        return "SMS";
    }

    @Override
    public String getChannelName() {
        return "短信通知";
    }

    @Override
    public boolean support(String channelCode) {
        return "SMS".equals(channelCode);
    }

    @Override
    public void send(String from, String[] to, String title,
                     String msg, String businessKey) {
        doSend(from, to, title, msg, businessKey);
    }

    @Override
    public void doSend(String from, String[] to, String title,
                       String msg, String businessKey) {
        // 调用短信服务商 API 发送
        for (String phone : to) {
            smsClient.send(phone, msg);
        }
    }

    @Override
    public boolean beforeSend(String from, String[] to,
                              String title, String msg) {
        return true; // 发送前校验
    }

    @Override
    public void afterSend(String from, String[] to,
                          String title, String msg) {
        // 发送后处理（如记录日志）
    }

    @Override
    public void read(String msgId) {
        // 短信无需已读操作
    }

    @Override
    public void readAll() {
    }

    @Override
    public Page<Message> myMessagePaged(Criteria criteria, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<Message> myMessage(Criteria criteria) {
        return List.of();
    }

    @Override
    public Page<Message> myUnreadPaged(Criteria criteria, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public List<Message> myUnread(Criteria criteria) {
        return List.of();
    }
}
```

## 6. 自定义 WebSocket 管理

实现 `MxpioWebSocketManager` 使用其他消息中间件同步：

```java
@Component
public class RabbitMqWebSocketManager implements MxpioWebSocketManager {
    // 使用 RabbitMQ 跨节点同步 WebSocket 消息
    // 替代默认的 InMemory/Redis 实现
}
```

## 7. 表达式引擎扩展

`mxpio-expression` 模块基于 Aviator 表达式引擎。可注册自定义函数：

```java
// 注册 Aviator 自定义函数
AviatorEvaluator.addFunction(new AbstractFunction() {
    @Override
    public String getName() {
        return "encrypt";
    }

    @Override
    public Object call(Map<String, Object> env, Object arg) {
        return encryptUtil.encrypt(arg.toString());
    }
});
```

## 8. 多租户扩展

`mxpio-multitenant` 模块提供了多租户基础支持，可自定义：

- `OrganizationKeyGenerator` — 租户标识生成策略
- `CurrentOrganizationStrategy` — 当前租户解析策略
- `MultitenantUserDetailsService` — 多租户用户加载
