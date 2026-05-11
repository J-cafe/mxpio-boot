# RocketMQ 配置

> RocketMQ 在 mxpio-boot 技术栈中列为消息队列组件，但当前 `dev/mxpio4.0` 分支的 `pom.xml` 中**未包含 RocketMQ 相关依赖**。本文说明如何引入 RocketMQ 支持。

---

## 1. 当前消息系统现状

当前 mxpio-boot 的消息处理使用**内部通道 + WebSocket** 架构：

```
业务模块 ──→ 内部 MessageChannel ──→ WebSocket ──→ 客户端
```

- 消息的发送与消费在同一个 JVM 进程内完成
- 实时推送通过 `mxpio-websocket` 模块实现
- 适用于单体应用或简单集群场景

如果需要在**生产环境中使用消息队列**实现异步解耦、削峰填谷、跨语言通信等能力，可引入 RocketMQ。

---

## 2. RocketMQ 依赖引入

### 方式一：使用 RocketMQ Spring Boot Starter

在模块的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>${rocketmq.version}</version>
</dependency>
```

然后在 `mxpio-dependencies/pom.xml` 的 `<dependencyManagement>` 中统一管理版本：

```xml
<rocketmq.version>4.9.7</rocketmq.version>
```

### 方式二：使用 Spring Cloud Stream

添加 Binder 依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rocketmq</artifactId>
</dependency>
```

---

## 3. 配置模板

### 3.1 基础配置（application-dev.yml）

```yaml
rocketmq:
  name-server: 127.0.0.1:9876            # NameServer 地址
  producer:
    group: mboot-producer-group           # 生产者组
    send-message-timeout: 3000            # 发送超时（毫秒）
    retry-times-when-send-failed: 2       # 发送失败重试次数
  consumer:
    group: mboot-consumer-group           # 消费者组
    listen-batch-size: 1                  # 批量消费大小
```

### 3.2 Spring Cloud Stream 配置

```yaml
spring:
  cloud:
    stream:
      bindings:
        messageOutput:
          destination: mboot-message-topic   # Topic 名称
          content-type: application/json
        messageInput:
          destination: mboot-message-topic
          content-type: application/json
          group: mboot-message-group
      rocketmq:
        binder:
          name-server: 127.0.0.1:9876
        bindings:
          messageOutput:
            producer:
              sync: true                  # 同步发送
          messageInput:
            consumer:
              orderly: true               # 顺序消费
```

---

## 4. 生产者示例

```java
@Component
public class MessageProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送系统通知消息
     */
    public void sendSystemNotice(SystemNotice notice) {
        Message<SystemNotice> message = MessageBuilder
            .withPayload(notice)
            .setHeader("type", "SYSTEM_NOTICE")
            .build();

        rocketMQTemplate.syncSend("mboot-system-notice:tag-notice", message);
    }

    /**
     * 发送异步消息（不等待结果）
     */
    public void sendAsyncMessage(Object payload) {
        rocketMQTemplate.asyncSend("mboot-async-topic", payload,
            new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.debug("Message sent: {}", sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    log.error("Message send failed", e);
                }
            });
    }

    /**
     * 发送延时消息（30秒后消费）
     */
    public void sendDelayMessage(Object payload) {
        // messageDelayLevel: 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        rocketMQTemplate.syncSend("mboot-delay-topic",
            MessageBuilder.withPayload(payload).build(),
            3000,
            4);   // 4 = 30秒 (level 3=10s, level 4=30s)
    }
}
```

---

## 5. 消费者示例

```java
@Component
@RocketMQMessageListener(
    topic = "mboot-system-notice",
    consumerGroup = "mboot-notice-consumer",
    selectorExpression = "tag-notice || tag-alert"
)
public class NoticeConsumer implements RocketMQListener<SystemNotice> {

    @Override
    public void onMessage(SystemNotice notice) {
        log.info("Received notice: {}", notice);
        // 处理通知逻辑
        // 例如：通过 WebSocket 推送给在线用户
        noticeService.pushToUser(notice);
    }
}
```

---

## 6. 集成到现有消息体系

如果需要将 RocketMQ 集成到当前的消息体系中，可以修改 `mxpio-message` 模块的 `MessageChannel` 实现：

```java
@Component
@ConditionalOnClass(name = "org.apache.rocketmq.spring.core.RocketMQTemplate")
public class RocketMQMessageChannel implements MessageChannel {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public boolean send(Message<?> message) {
        // 将内部消息转发到 RocketMQ
        SendResult result = rocketMQTemplate.syncSend(
            "mboot-internal-topic", message.getPayload());
        return result.getSendStatus() == SendStatus.SEND_OK;
    }

    @Override
    public boolean send(Message<?> message, long timeout) {
        SendResult result = rocketMQTemplate.syncSend(
            "mboot-internal-topic", message.getPayload(), timeout);
        return result.getSendStatus() == SendStatus.SEND_OK;
    }
}
```

---

## 7. 是否选择 RocketMQ？

| 场景 | 建议 |
|------|------|
| **单体应用、小规模集群** | 当前内部通道 + WebSocket 已够用 |
| **大规模分布式部署** | 建议引入 RocketMQ |
| **需要消息持久化、可靠投递** | 建议引入 RocketMQ |
| **与异构系统通信** | 建议引入 RocketMQ |
| **简单异步任务** | 可使用 Spring `@Async` 或 `ApplicationEventPublisher` |

---

## 8. 快速部署 RocketMQ 开发环境

```bash
# 下载 RocketMQ（以 4.9.7 为例）
wget https://archive.apache.org/dist/rocketmq/4.9.7/rocketmq-all-4.9.7-bin-release.zip
unzip rocketmq-all-4.9.7-bin-release.zip
cd rocketmq-4.9.7

# 启动 NameServer
nohup bin/mqnamesrv > namesrv.log 2>&1 &

# 启动 Broker
nohup bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true > broker.log 2>&1 &

# 验证
tail -f ~/logs/rocketmqlogs/namesrv.log
tail -f ~/logs/rocketmqlogs/broker.log
```

> Docker 一键部署方式请参考 [RocketMQ Docker](https://hub.docker.com/r/apache/rocketmq)。
