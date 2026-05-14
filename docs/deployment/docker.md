# Docker 部署

> 使用 Docker 容器化部署 mxpio-boot 应用，包含 MySQL 和 Redis 依赖。

---

## 1. 多阶段构建 Dockerfile

在项目根目录创建 `Dockerfile`：

```dockerfile
# ========== 第一阶段：构建 ==========
FROM zulu-openjdk:25 AS builder

WORKDIR /build

# 复制 Maven Wrapper 和 POM
COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY mxpio-dependencies/pom.xml mxpio-dependencies/
COPY mxpio-framework/mxpio-autoconfigure/pom.xml mxpio-framework/mxpio-autoconfigure/

# 复制框架模块 POM
COPY mxpio-framework/mxpio-common/pom.xml mxpio-framework/mxpio-common/
COPY mxpio-framework/mxpio-jpa/pom.xml mxpio-framework/mxpio-jpa/
COPY mxpio-framework/mxpio-security/pom.xml mxpio-framework/mxpio-security/
COPY mxpio-framework/mxpio-cache/pom.xml mxpio-framework/mxpio-cache/
COPY mxpio-framework/mxpio-cache-redis/pom.xml mxpio-framework/mxpio-cache-redis/
COPY mxpio-framework/mxpio-cache-caffeine/pom.xml mxpio-framework/mxpio-cache-caffeine/
COPY mxpio-framework/mxpio-quartz/pom.xml mxpio-framework/mxpio-quartz/
COPY mxpio-framework/mxpio-log/pom.xml mxpio-framework/mxpio-log/
COPY mxpio-framework/mxpio-camunda/pom.xml mxpio-framework/mxpio-camunda/
COPY mxpio-framework/mxpio-websocket/pom.xml mxpio-framework/mxpio-websocket/
COPY mxpio-framework/mxpio-message/pom.xml mxpio-framework/mxpio-message/
COPY mxpio-framework/mxpio-multitenant/pom.xml mxpio-framework/mxpio-multitenant/
COPY mxpio-framework/mxpio-filestorage/pom.xml mxpio-framework/mxpio-filestorage/
COPY mxpio-framework/mxpio-excel/pom.xml mxpio-framework/mxpio-excel/
COPY mxpio-framework/mxpio-expression/pom.xml mxpio-framework/mxpio-expression/
COPY mxpio-framework/mxpio-dbconsole/pom.xml mxpio-framework/mxpio-dbconsole/
COPY mxpio-framework/mxpio-datav/pom.xml mxpio-framework/mxpio-datav/

# 复制业务模块 POM
COPY mxpio-boot-modules/mxpio-dingtalk/pom.xml mxpio-boot-modules/mxpio-dingtalk/
COPY mxpio-boot-modules/mxpio-wechat/pom.xml mxpio-boot-modules/mxpio-wechat/
COPY mxpio-boot-modules/mxpio-email/pom.xml mxpio-boot-modules/mxpio-email/
COPY mxpio-boot-modules/mxpio-msal/pom.xml mxpio-boot-modules/mxpio-msal/
COPY mxpio-boot-modules/mxpio-oauth/pom.xml mxpio-boot-modules/mxpio-oauth/

# 复制应用 POM
COPY mxpio-boot-app/mxpio-boot-webapp/pom.xml mxpio-boot-app/mxpio-boot-webapp/

# 下载依赖（利用 Docker 层缓存）
RUN mvn dependency:go-offline -DskipTests -B || true

# 复制源码并构建
COPY . .
RUN mvn clean package -DskipTests -pl mxpio-boot-app/mxpio-boot-webapp -am -B

# ========== 第二阶段：运行 ==========
FROM zulu-openjdk:25-jre

WORKDIR /app

# 从构建阶段复制 JAR
COPY --from=builder /build/mxpio-boot-app/mxpio-boot-webapp/target/mxpio-boot-webapp-*.jar app.jar

# 创建日志和数据目录
RUN mkdir -p /app/logs /app/uploads

# 暴露端口
EXPOSE 9005

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:9005/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 2. Docker Compose 部署

### 2.1 完整示例

在项目根目录创建 `docker-compose.yml`：

```yaml
version: '3.8'

services:
  # ====== MySQL ======
  mysql:
    image: mysql:8.0
    container_name: mboot-mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: mxp9827459
      MYSQL_DATABASE: mboot
      MYSQL_CHARACTER_SET_SERVER: utf8mb4
      MYSQL_COLLATION_SERVER: utf8mb4_unicode_ci
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./sql/init:/docker-entrypoint-initdb.d   # 可选的初始化 SQL
    networks:
      - mboot-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  # ====== Redis ======
  redis:
    image: redis:7.2
    container_name: mboot-redis
    restart: unless-stopped
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    networks:
      - mboot-network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5

  # ====== 应用 ======
  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: mboot-app
    restart: unless-stopped
    ports:
      - "9005:9005"
    environment:
      # 激活 profile
      - SPRING_PROFILES_ACTIVE=prod

      # 数据库配置（环境变量覆盖）
      - MYSQL_HOST=mysql
      - MYSQL_PORT=3306
      - MYSQL_DATABASE=mboot
      - MYSQL_USERNAME=root
      - MYSQL_PASSWORD=mxp9827459

      # Redis 配置
      - REDIS_HOST=redis
      - REDIS_PORT=6379
      - REDIS_PASSWORD=

      # JVM 参数
      - JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseZGC
    volumes:
      - app-logs:/app/logs        # 应用日志
      - app-uploads:/app/uploads  # 上传文件
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy
    networks:
      - mboot-network

volumes:
  mysql-data:
  redis-data:
  app-logs:
  app-uploads:

networks:
  mboot-network:
    driver: bridge
```

### 2.2 启动

```bash
# 构建并启动所有服务
docker compose up -d

# 查看日志
docker compose logs -f app

# 查看各服务状态
docker compose ps

# 停止所有服务
docker compose down
```

---

## 3. 环境变量覆盖

mxpio-boot 的配置均支持通过环境变量覆盖。以下为完整的可覆盖变量列表：

### 数据库相关

| 环境变量 | 对应配置项 | 默认值 |
|----------|-----------|--------|
| `SPRING_PROFILES_ACTIVE` | `spring.profiles.active` | `dev` |
| `MYSQL_HOST` | `spring.datasource.url` 中的主机 | `localhost` |
| `MYSQL_PORT` | `spring.datasource.url` 中的端口 | `3306` |
| `MYSQL_DATABASE` | `spring.datasource.url` 中的数据库名 | `mboot` |
| `MYSQL_USERNAME` | `spring.datasource.username` | `root` |
| `MYSQL_PASSWORD` | `spring.datasource.password` | `mxp9827459` |

### Redis 相关

| 环境变量 | 对应配置项 | 默认值 |
|----------|-----------|--------|
| `REDIS_HOST` | `spring.data.redis.host` | `localhost` |
| `REDIS_PORT` | `spring.data.redis.port` | `6379` |
| `REDIS_PASSWORD` | `spring.data.redis.password` | （空） |

### 应用相关

| 环境变量 | 对应配置项 | 默认值 |
|----------|-----------|--------|
| `SERVER_PORT` | `server.port` | `9005` |
| `JAVA_OPTS` | JVM 启动参数 | （空） |

### 实现方式

这些环境变量通过 `application-prod.yml` 中的占位符引用：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:mboot}?characterEncoding=utf-8&useSSL=true&nullCatalogMeansCurrent=true
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:mxp9827459}

  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      password: ${REDIS_PASSWORD:}
```

---

## 4. 优化技巧

### 4.1 启用 Spring Boot Actuator

在 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

配置健康检查端点：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

### 4.2 日志持久化

确保日志卷挂载正确后，在 `application-prod.yml` 中配置日志输出：

```yaml
logging:
  file:
    path: /app/logs
    name: /app/logs/mxpio-boot.log
  level:
    com.mxpio: INFO
    org.springframework: WARN
```

### 4.3 上传文件目录

在 `application-prod.yml` 中配置文件存储路径：

```yaml
mxpio:
  filestorage:
    local:
      upload-dir: /app/uploads
```

---

## 5. 常用命令速查

```bash
# 构建并启动
docker compose up -d

# 重新构建应用（修改代码后）
docker compose build app
docker compose up -d app

# 查看实时日志
docker compose logs -f app

# 进入容器
docker compose exec app bash

# 停止并删除容器
docker compose down

# 清理数据卷（谨慎：会删除所有数据）
docker compose down -v

# 仅重启应用
docker compose restart app

# 查看资源占用
docker compose stats
```

---

## 6. 常见问题

### Q1: 容器启动后立即退出

**解决**：查看日志分析原因：

```bash
docker compose logs app
```

常见原因：
- 数据库连接失败（检查 MYSQL_HOST 和密码）
- Redis 连接失败
- 端口冲突

### Q2: 数据库初始化慢

**原因**：首次启动时 Hibernate `ddl-auto: update` + SQL 脚本初始化。

**解决**：可通过健康检查等待数据库就绪后再启动应用（已在 docker-compose.yml 中配置 `depends_on.condition: service_healthy`）。

### Q3: 上传文件丢失

**原因**：容器重启后，容器内文件系统重置。

**解决**：确保上传目录挂在持久化卷上（已在 docker-compose.yml 中配置 `app-uploads` 卷）。
