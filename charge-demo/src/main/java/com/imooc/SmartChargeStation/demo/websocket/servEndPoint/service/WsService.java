package com.imooc.SmartChargeStation.demo.websocket.servEndPoint.service;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * author: Imooc
 * description: WebSocket(基于Tomcat @ServerEndPoint) 服务类
 * date: 2024
 */

@Component
@Slf4j
@ServerEndpoint("/ws/server")
public class WsService {

    /* **********************
     *
     * @ServerEndpoint修饰的类，
     * 包含@Open @Close @OnMessage @OnError方法
     *
     * 疑问？
     * Springboot如何能将@ServerEndpoint修饰的类
     * 注入到容器里？
     *
     * *********************/

    @OnOpen
    public void onOpen()
    {

        log.info(">>>>ServerEndPoint 连接建立成功！<<<<");

    }

    @OnClose
    public void onClose()
    {

    }

    @OnMessage
    public void onMessage(String message)
    {
    }

    @OnError
    public void onError(Throwable error)
    {

    }


}
