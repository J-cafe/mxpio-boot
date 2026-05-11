# mxpio-websocket — 实时消息推送

> `mxpio-websocket` 模块提供基于 WebSocket 的实时消息推送能力，
> 支持单机内存模式和集群 Redis 模式。

---

## 模块说明

WebSocket 是服务端主动向客户端推送消息的标准协议。mxpio-boot 对该能力进行了封装，提供统一的连接管理接口，开发者无需处理底层 WebSocket 细节。

与 `mxpio-message` 消息系统配合使用可实现完整的通知推送方案。

## Maven 依赖

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-websocket</artifactId>
</dependency>
```

## 核心 API

### MxpioWebSocketManager

```java
public interface MxpioWebSocketManager {
    WebSocketConnection get(String endpoint, String id);
    void put(String endpoint, String id, WebSocketConnection webSocket);
    void remove(String endpoint, String id);
    int size(String endpoint);

    // 发送消息（指定用户）
    void send(String endpoint, String id, String text);
    void send(String id, String text);

    // 广播消息（所有连接）
    void broadcast(String endpoint, String text);
    void broadcast(String text);

    // 向端点发送
    void sendToEndpoint(String endpoint, String id, String text);
    void sendToEndpoint(String id, String textIv);
    void broadcastToEndpoint(String endpoint, String text);
    void broadcastToEndpoint(String text);
}
```

### 方法说明

| 方法 | 说明 | 参数 |
|------|------|------|
| `send(id, text)` | 向指定连接发送消息 | id: 用户/连接标识, text: 消息内容 |
| `broadcast(text)` | 向所有连接广播消息 | text: 消息内容 |
| `sendToEndpoint(endpoint, id, text)` | 向指定端点的用户发消息 | endpoint: 端点名 |
| `broadcastToEndpoint(endpoint, text)` | 向指定端点所有连接广播 | endpoint: 端点名 |

## 实现模式

### 内存模式（默认，单节点适用）

```java
// 系统自动注入 InMemoryMxpioWebSocketManagerImpl
@Autowired
private MxpioWebSocketManager webSocketManager;
```

条件注解：`WebsocketInMemoryModeCondition`

### Redis 模式（集群部署时使用）

引入 Redis 依赖后自动切换为 `RedisMxpioWebSocketManagerImpl`，通过 Redis Pub/Sub 在多个节点间同步消息。

条件注解：`WebsocketRedisModeCondition`

## 使用示例

### 1. 后端主动推送

```java
@Service
public class NotificationService {

    @Autowired
    private MxpioWebSocketManager webSocketManager;

    // 向特定用户推送
    public void notifyUser(String userId, String message) {
        webSocketManager.send(userId, message);
    }

    // 广播系统通知
    public void broadcastSystemNotice(String notice) {
        webSocketManager.broadcast(notice);
    }

    // 向特定模块端点推送
    public void notifyOrderModule(String userId, String orderInfo) {
        webSocketManager.sendToEndpoint("order", userId, orderInfo);
    }
}
```

### 2. 前端连接（JavaScript）

```javascript
// 建立 WebSocket 连接
const socket = new WebSocket('ws://localhost:9005/ws?token=user-jwt-token');

socket.onopen = function() {
    console.log('WebSocket 连接已建立');
};

socket.onmessage = function(event) {
    const message = JSON.parse(event.data);
    console.log('收到消息:', message);
    // 处理消息（弹窗提醒、更新角标等）
};

socket.onclose = function() {
    console.log('WebSocket 连接已关闭');
};
```

## 与消息系统集成

WebSocket 通常与消息系统配合使用：

```java
// 发送站内信 + WebSocket 推送
@Autowired
private MessageChannel innerChannel;

@Autowired
private MxpioWebSocketManager webSocketManager;

public void sendNotification(String userId, String title, String content) {
    // 1. 保存消息到数据库
    innerChannel.send("system", new String[]{userId}, title, content, null trig);

    // 2. WebSocket 实时推送
    webSocketManager.send(userId, JSON.toJSONString(Map.of(
        "type", "notification",
        "title", title,
        "content", content
    )));
}
```
