# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

智慧充电站系统 - 基于 Java 17 和 Spring Boot 3.0.2 的多模块 Maven 项目，实现充电桩与服务器之间的通信。

## 常用命令

```bash
# 构建整个项目
mvn clean install

# 构建单个模块
mvn clean install -pl charge-mqtt-client

# 跳过测试构建
mvn clean install -DskipTests

# 运行测试
mvn test

# 运行单个模块的测试
mvn test -pl charge-demo
```

## 模块架构

项目采用多模块结构，各模块职责：

- **charge-demo**: 示例模块，包含 MQTT（Paho 和工厂两种模式）、WebSocket、Netty 的使用示例
- **charge-netty-server**: Netty 服务端，通过 WebSocket 与小程序通信（端口 8989）
- **charge-mqtt-client**: MQTT 客户端，使用 Spring Integration 与 EMQX 消息服务器通信
- **charge-protocol**: 私有通信协议定义模块，定义 `ChargePayload` 消息格式
- **charge-iotdb**: Apache IoTDB 时序数据库集成模块

## 核心技术栈

- **Netty 4.1.86**: TCP/WebSocket 通信
- **Spring Integration MQTT**: 基于 Eclipse Paho 的 MQTT 客户端
- **Apache IoTDB 1.3.2**: 时序数据存储
- **Protobuf**: 数据序列化
- **Lombok**: 代码简化

## 通信架构

系统采用双通道通信：

1. **MQTT 通道**: charge-mqtt-client 通过 EMQX（默认 tcp://emqx:1883）与充电桩通信，使用主题 `charge/cmd`
2. **WebSocket 通道**: charge-netty-server 通过 Netty（端口 8989）与小程序前端通信

## 私有协议

`ChargePayload` 定义了充电桩消息格式：
- 开始标志 (0x76)、协议版本、充电桩 ID、消息类型（1=操作指令，2=充电状态）
- 充电状态数据（最多 500 字节）、操作指令、校验码

## 配置说明

各模块的 `application.yml` 配置：
- MQTT 连接参数（host、client_id、topic）在 `MqttProps` 类中通过 `@ConfigurationProperties` 绑定
- Netty 服务端口通过 `Netty.server.port` 配置
- IoTDB 连接参数（ip、port、username、password）在 `IotDBSessionConf` 中配置

## 测试

测试使用 JUnit 5 + Spring Boot Test，示例测试位于 `charge-demo/src/test/` 目录，使用 MockMvc 进行接口测试。
