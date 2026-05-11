# mxpio-cache — 统一缓存抽象模块

## 概述

`mxpio-cache` 提供了一套统一的缓存 API 抽象层，支持 Redis 和 Caffeine 两种后端实现。开发者可以通过统一的 `CacheProvider` 接口操作缓存，无需关注底层实现细节，同时支持 Spring 的 `@Cacheable`、`@CacheEvict` 等声明式缓存注解。

### 缓存架构

```
┌─────────────────────────────────────┐
│         Application Code            │
├──────────────────┬──────────────────┤
│  CacheProvider   │ @Cacheable etc.  │
│  (统一 API)      │ (Spring 注解)     │
├────────┬─────────┴─────────┬────────┤
│  Redis │    Caffeine       │        │
│ (远程) │    (本地)         │  (扩展) │
└────────┴──────────────────┴─────────┘
```

---

## Maven 依赖

### 核心 API（必须引入）

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-cache</artifactId>
</dependency>
```

### Redis 实现（引入即启用 Redis 缓存）

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-cache-redis</artifactId>
</dependency>
```

### Caffeine 实现（引入即启用 Caffeine 本地缓存）

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-cache-caffeine</artifactId>
</dependency>
```

> **自动配置说明**：当 classpath 中存在 `mxpio-cache-redis` 时，自动配置 Redis 缓存实现；存在 `mxpio-cache-caffeine` 时，自动配置 Caffeine 缓存实现。两者可以共存。

---

## CacheProvider 接口

`CacheProvider` 是统一缓存操作的核心接口，提供了以下方法：

| 方法                                          | 说明                              |
|-----------------------------------------------|-----------------------------------|
| `boolean hasKey(String key)`                  | 判断缓存 key 是否存在             |
| `boolean del(String key)`                     | 删除指定 key 的缓存               |
| `<T> T get(String key, Class<T> clazz)`       | 获取缓存值                        |
| `void set(String key, Object value)`          | 设置缓存（永不过期）              |
| `void set(String key, Object value, long time)` | 设置缓存（秒级过期时间）        |
| `void set(String key, Object value, long time, TimeUnit timeUnit)` | 设置缓存（自定义时间单位） |

### 基本使用示例

```java
import com.mxpio.cache.CacheProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService {

    @Autowired
    private CacheProvider cacheProvider;

    /**
     * 缓存用户信息
     */
    public void cacheUser(User user) {
        String key = "user:" + user.getId();
        // 缓存 30 分钟
        cacheProvider.set(key, user, 30, TimeUnit.MINUTES);
    }

    /**
     * 获取缓存的用户
     */
    public User getCachedUser(Long userId) {
        String key = "user:" + userId;
        return cacheProvider.get(key, User.class);
    }

    /**
     * 判断用户是否已缓存
     */
    public boolean isUserCached(Long userId) {
        return cacheProvider.hasKey("user:" + userId);
    }

    /**
     * 删除用户缓存
     */
    public void removeUserCache(Long userId) {
        cacheProvider.del("user:" + userId);
    }
}
```

### 设置永不过期的缓存

```java
// 永不过期（取决于底层实现的驱逐策略）
cacheProvider.set("config:system", systemConfig);
```

### 设置带过期时间的缓存

```java
// 秒级过期
cacheProvider.set("session:" + token, sessionData, 3600); // 1小时

// 自定义时间单位
cacheProvider.set("temp:code", verificationCode, 5, TimeUnit.MINUTES);
cacheProvider.set("rate:limit:ip", count, 1, TimeUnit.DAYS);
```

---

## Spring 声明式缓存

`mxpio-cache` 完全兼容 Spring 的声明式缓存注解：

### @Cacheable — 缓存方法结果

```java
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    /**
     * 查询用户，结果自动缓存
     * cacheNames 指定缓存区域，key 使用 SpEL 表达式
     */
    @Override
    @Cacheable(cacheNames = "user", key = "#id")
    public User getUserById(Long id) {
        // 如果缓存命中，不会执行此方法
        System.out.println("从数据库查询用户: " + id);
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 复杂 key 表达式
     */
    @Cacheable(cacheNames = "user", key = "'page:' + #page + ':size:' + #size")
    public List<User> listUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}
```

### @CacheEvict — 清除缓存

```java
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    /**
     * 更新用户后清除对应缓存
     */
    @Override
    @CacheEvict(cacheNames = "user", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 删除用户后清除缓存
     */
    @Override
    @CacheEvict(cacheNames = "user", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 清除整个缓存区域
     */
    @Override
    @CacheEvict(cacheNames = "user", allEntries = true)
    public void clearAllUserCache() {
        // 清空 user 区域的所有缓存
    }
}
```

### @CachePut — 更新缓存（不拦截方法执行）

```java
import org.springframework.cache.annotation.CachePut;

/**
 * 每次调用都会执行方法，并将结果更新到缓存
 * 适用于数据更新场景
 */
@Override
@CachePut(cacheNames = "user", key = "#user.id")
public User saveUser(User user) {
    return userRepository.save(user);
}
```

### 组合使用

```java
@Cacheable(cacheNames = "user", key = "#id")
public User getUserById(Long id) { ... }

@CacheEvict(cacheNames = "user", key = "#id")
public void deleteUser(Long id) { ... }

@CacheEvict(cacheNames = "user", allEntries = true)
public void refreshAllUserCache() { ... }
```

---

## 自动配置

### 条件自动装配

- 当 classpath 包含 `mxpio-cache-redis` 时，自动配置 `RedisCacheProvider`
- 当 classpath 包含 `mxpio-cache-caffeine` 时，自动配置 `CaffeineCacheProvider`

如果同时引入两者，可通过 `@Primary` 或 `@Qualifier` 指定优先使用的实现：

```java
@Autowired
@Qualifier("redisCacheProvider")
private CacheProvider cacheProvider;
```

---

## 配置文件

### Redis 实现配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    password:
    timeout: 5000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0

# mxpio 缓存全局默认过期时间（可选）
mxpio:
  cache:
    redis:
      default-expire: 3600      # 默认过期时间（秒）
      key-prefix: "mxpio:cache:" # key 前缀
```

### Caffeine 实现配置

```yaml
mxpio:
  cache:
    caffeine:
      maximum-size: 10000        # 最大缓存条目数
      expire-after-write: 3600   # 写入后过期时间（秒）
      initial-capacity: 100      # 初始容量
```

| 配置项                                      | 默认值   | 说明                        |
|---------------------------------------------|----------|-----------------------------|
| `spring.redis.host`                         | `localhost` | Redis 服务器地址          |
| `spring.redis.port`                         | `6379`   | Redis 服务器端口             |
| `mxpio.cache.redis.default-expire`          | `3600`   | Redis 缓存默认过期时间（秒） |
| `mxpio.cache.redis.key-prefix`              | `""`     | Redis key 前缀               |
| `mxpio.cache.caffeine.maximum-size`         | `10000`  | Caffeine 最大缓存条目数      |
| `mxpio.cache.caffeine.expire-after-write`   | `3600`   | Caffeine 写入后过期时间（秒）|
| `mxpio.cache.caffeine.initial-capacity`     | `100`    | Caffeine 初始容量            |

---

## 完整示例：同时使用两种缓存实现

```java
@Service
public class HybridCacheService {

    @Autowired
    @Qualifier("redisCacheProvider")
    private CacheProvider redisCache;      // 远程缓存（共享）

    @Autowired
    @Qualifier("caffeineCacheProvider")
    private CacheProvider localCache;      // 本地缓存（速度快）

    /**
     * 两级缓存策略：先查本地，再查 Redis
     */
    public Object getData(String key) {
        // 1. 先查本地缓存
        Object local = localCache.get(key, Object.class);
        if (local != null) {
            return local;
        }

        // 2. 再查 Redis
        Object remote = redisCache.get(key, Object.class);
        if (remote != null) {
            // 回填到本地缓存
            localCache.set(key, remote, 60);
            return remote;
        }

        return null;
    }
}
```

---

## 依赖关系

```
mxpio-cache (core API)
├── mxpio-common
├── spring-boot-starter-cache
└── spring-context-support

mxpio-cache-redis
├── mxpio-cache
└── spring-boot-starter-data-redis

mxpio-cache-caffeine
├── mxpio-cache
└── caffeine
```
