# Imooc Smart Charge Station

智慧充电站系统 - 基于 Java 17 + Spring Boot 3.0.2 的多模块 Maven 项目，实现充电桩与服务器之间的双向通信。

## 技术栈

- **Java 17** + **Spring Boot 3.0.2**
- **Netty 4.1.86** - TCP/WebSocket 通信
- **Spring Integration MQTT** - 基于 Eclipse Paho 的 MQTT 客户端
- **Apache IoTDB 1.3.2** - 时序数据库
- **Protobuf** - 数据序列化
- **Lombok** - 代码简化

## 项目结构

```
Imooc-Smart-Charge-Station/
├── charge-demo           # 示例模块（MQTT、WebSocket、Netty 使用示例）
├── charge-netty-server   # Netty 服务端（与小程序 WebSocket 通信）
├── charge-mqtt-client    # MQTT 客户端（与 EMQX 消息服务器通信）
├── charge-protocol       # 私有通信协议定义
├── charge-iotdb          # IoTDB 时序数据库集成
└── pom.xml               # 父 POM
```

## 模块说明

### charge-demo
演示模块，包含：
- MQTT 两种使用模式（Paho 直连模式 / Spring Integration 工厂模式）
- WebSocket 实现（@ServerEndpoint / Spring 封装）
- Netty 客户端/服务端示例
- Protobuf 序列化示例

### charge-netty-server
Netty 服务端，通过 WebSocket 与小程序前端通信，默认端口 8989。

### charge-mqtt-client
MQTT 客户端，使用 Spring Integration 工厂模式与 EMQX 通信，订阅主题 `charge/cmd`。

### charge-protocol
定义私有通信协议 `ChargePayload`：
- 开始标志 (0x76)、协议版本、充电桩 ID
- 消息类型（1=操作指令，2=充电状态）
- 充电状态数据（最多 500 字节）、操作指令、校验码

### charge-iotdb
Apache IoTDB 时序数据库集成，提供充电数据的存储和查询。

## 环境要求

- JDK 17
- Maven 3.8+
- EMQX（MQTT 消息服务器）
- Apache IoTDB 1.3.2

## 快速开始

### 构建项目

```bash
# 构建整个项目
mvn clean install

# 跳过测试构建
mvn clean install -DskipTests

# 构建单个模块
mvn clean install -pl charge-mqtt-client
```

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行单个模块的测试
mvn test -pl charge-demo
```

### 启动服务

1. **启动 MQTT 客户端**：运行 `charge-mqtt-client` 模块的 `MqttApplication`
2. **启动 Netty 服务端**：运行 `charge-netty-server` 模块的 `NettyApplication`

## 配置说明

### MQTT 配置 (charge-mqtt-client/src/main/resources/application.yml)

```yaml
mqtt:
  host: tcp://emqx:1883        # EMQX 服务器地址
  client_id: imooc_charge_station_mqtt
  topic: charge/cmd             # 订阅主题
```

### Netty 配置 (charge-netty-server/src/main/resources/application.yml)

```yaml
Netty:
  server:
    port: 8989                  # WebSocket 监听端口
```

### IoTDB 配置 (charge-iotdb/src/main/resources/application.yml)

```yaml
iotdb:
  username: root
  password: root
  ip: iotdb
  port: 6667
```

## 通信架构

```
┌─────────────┐     MQTT      ┌─────────────┐     TCP/WebSocket     ┌─────────────┐
│   充电桩     │ ◄───────────► │    EMQX     │                       │   小程序     │
└─────────────┘               └─────────────┘                       └─────────────┘
                                      ▲                                     ▲
                                      │ MQTT                                │ WebSocket
                                      ▼                                     ▼
                              ┌─────────────┐                       ┌─────────────┐
                              │ charge-mqtt │                       │ charge-netty│
                              │   -client   │                       │   -server   │
                              └─────────────┘                       └─────────────┘
```

## 许可证

本项目仅供学习使用。
