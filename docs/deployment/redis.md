# Redis 配置

> mxpio-boot 使用 Redis 7.2.x+ 作为缓存和会话存储中间件。

---

## 1. 环境要求

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| **Redis** | 7.2.x+ | 建议使用最新稳定版 |
| **Redis 客户端** | Lettuce（内置） | Spring Boot 默认集成，无需额外安装 |

---

## 2. 配置

### 2.1 开发环境配置

文件位置：`examples/mxpio-boot-example/src/main/resources/application-dev.yml`

```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:                       # 留空，不设密码
      lettuce:
        pool:
          max-active: 8               # 连接池最大连接数
          max-wait: -1                # 获取连接最大等待时间（-1 表示无限等待）
          max-idle: 8                 # 连接池最大空闲连接数
          min-idle: 0                 # 连接池最小空闲连接数
```

### 2.2 配置项说明

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `spring.data.redis.host` | `127.0.0.1` | Redis 服务器主机地址 |
| `spring.data.redis.port` | `6379` | Redis 服务器端口 |
| `spring.data.redis.password` | 空 | Redis 密码（生产环境务必设置） |
| `spring.data.redis.lettuce.pool.max-active` | `8` | 最大活跃连接数 |
| `spring.data.redis.lettuce.pool.max-wait` | `-1` | 获取连接最大等待时间（负值表示无限） |
| `spring.data.redis.lettuce.pool.max-idle` | `8` | 最大空闲连接数 |
| `spring.data.redis.lettuce.pool.min-idle` | `0` | 最小空闲连接数 |

### 2.3 生产环境配置

```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      password: ${REDIS_PASSWORD:}     # 建议从环境变量注入
      timeout: 5000                    # 连接超时（毫秒）
      lettuce:
        pool:
          max-active: 16
          max-wait: 3000
          max-idle: 16
          min-idle: 4
```

---

## 3. Redis 在本项目中的用途

| 用途 | 实现模块 | 说明 |
|------|---------|------|
| **缓存** | `mxpio-cache-redis` | 通过 `CacheProvider` 抽象层实现声明式缓存 |
| **WebSocket 集群** | `mxpio-websocket` | `RedisMxpioWebSocketManagerImpl` 管理集群 WebSocket 会话 |
| **Session 管理** | Spring Session | 分布式会话共享（可选） |

### 3.1 缓存（mxpio-cache-redis）

`mxpio-cache-redis` 模块实现了 `CacheProvider` 接口，提供基于 Redis 的缓存能力。配合 Spring 的 `@Cacheable`、`@CacheEvict`、`@CachePut` 注解使用：

```java
@Service
public class UserService {

    @Cacheable(value = "user", key = "#id")
    public User findById(Long id) {
        // 从数据库查询，结果自动缓存
        return userRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "user", key = "#user.id")
    public User update(User user) {
        return userRepository.save(user);
    }
}
```

### 3.2 WebSocket 集群（RedisMxpioWebSocketManagerImpl）

在多实例部署时，WebSocket 消息需要跨实例广播。`RedisMxpioWebSocketManagerImpl` 利用 Redis 的发布/订阅机制实现：

```
WebSocket Client A ──→ Instance 1 ──→ Redis Pub/Sub ──→ Instance 2 ──→ WebSocket Client B
```

### 3.3 Session 管理

通过 `spring-session-data-redis`，可以将 HTTP Session 存储在 Redis 中，实现多实例无状态会话共享。

---

## 4. 多级缓存

mxpio-boot 的缓存模块支持**两级缓存架构**：

```
┌─────────────────────────────────────────┐
│             应用层 (Caffeine)            │  ← 本地缓存，速度最快
├─────────────────────────────────────────┤
│             分布式层 (Redis)             │  ← 共享缓存，支持集群
└─────────────────────────────────────────┘
```

- **Caffeine** 作为一级缓存（本地 JVM 内），适合高频读取、不常变化的数据
- **Redis** 作为二级缓存（分布式），适合跨实例共享的数据

两级缓存的协调通过 `mxpio-cache` 的 `CacheProvider` 抽象层自动处理。

---

## 5. 生产环境安全建议

### 5.1 设置密码

```bash
# 编辑 redis.conf
requirepass your-strong-password-here

# 重启 Redis
redis-server /path/to/redis.conf
```

### 5.2 绑定内网地址

```bash
# redis.conf
bind 127.0.0.1 192.168.1.100    # 仅允许本机和内网访问
protected-mode yes
```

### 5.3 禁用危险命令

```bash
# redis.conf
rename-command FLUSHALL ""
rename-command FLUSHDB ""
rename-command CONFIG ""
```

### 5.4 连接加密（TLS）

```yaml
spring:
  data:
    redis:
      ssl: true
      # 如果配置了 TLS 证书
      # lettuce.ssl.trust-store: classpath:redis-truststore.jks
```

---

## 6. 验证连接

启动应用后，可在日志中确认 Redis 连接状态：

```log
2025-xx-xx xx:xx:xx  INFO  --- [main] io.lettuce.core.RedisClient             : Connecting to Redis at localhost:6379
2025-xx-xx xx:xx:xx  INFO  --- [main] io.lettuce.core.protocol.ConnectionWatchdog: Reconnecting, 1 more attempt(s)...
2025-xx-xx xx:xx:xx  INFO  --- [main] io.lettuce.core.RedisChannelHandler      : channelInitialised
```

或通过 Redis CLI 验证：

```bash
redis-cli ping
# 输出: PONG

redis-cli monitor
# 查看应用发出的 Redis 命令
```

---

## 7. 常见问题

### Q1: 启动报 Redis 连接失败

**解决**：
```bash
# 检查 Redis 是否运行
redis-cli ping

# 如果未运行，启动 Redis
redis-server

# 检查端口是否被占用
lsof -i :6379
```

### Q2: 连接池耗尽

**表现**：应用报 `RedisConnectionFailureException: Unable to connect to Redis`

**解决**：适当增大连接池配置：

```yaml
spring:
  data:
    redis:
      lettuce:
        pool:
          max-active: 32
          max-wait: 5000
```

### Q3: 缓存数据未实时更新

**原因**：Caffeine 本地缓存有 TTL，修改数据后需等待缓存过期。

**解决**：在修改业务数据的 Service 方法上添加 `@CacheEvict` 注解，及时清除缓存。
