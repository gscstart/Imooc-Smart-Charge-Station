package com.imooc.SmartChargeStation.netty.server.websocket;

import com.imooc.SmartChargeStation.netty.server.handlers.ImoocServerHeartBeatHandler;
import com.imooc.SmartChargeStation.netty.server.handlers.ImoocWebSocketInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


/**
 * author: Imooc
 * description: 自定义的通道初始化
 * date: 2024
 */

@Slf4j
public class ImoocChannelInit extends ChannelInitializer<SocketChannel> {
    /**
     * author: Imooc
     * description: 通道初始化
     * @param socketChannel:
     * @return void
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        /* **********************
         *
         * Channel初始化是伴随ChannelPipeline的初始化
         *
         * *********************/

        //取出ChannelPipeline
        ChannelPipeline pipeline = socketChannel.pipeline();

        /* **********************
         *
         * 真正进行业务逻辑处理
         *
         * *********************/

        pipeline
                //超时监听
                .addLast(new IdleStateHandler(3,3,3, TimeUnit.SECONDS))
                //自定义的超时处理逻辑
                .addLast(new ImoocServerHeartBeatHandler())
                /* **********************
                 *
                 * Netty内置的处理器
                 *
                 * 1. HttpServerCodec
                 *   Codec作为结尾，既可以做编码器，也可以做解码器
                 *
                 * 2. HttpObjectAggregator
                 *
                 * 3. WebSocketServerProtocolHandler
                 *    自动的将HTTP升级为WebSocket协议
                 *    自动的完成握手过程
                 *    以及后续数据帧的编码和解码工作
                 *
                 *
                 * *********************/
                //对HTTP协议的解析
                .addLast(new HttpServerCodec())

                /* **********************
                 *
                 * HTTP分为GET和POST
                 * GET的参数放在URL
                 * POST的参数放在Body
                 *
                 * HttpServerCodec只能解析URL参数
                 * 不能解析Body参数
                 *
                 * HttpObjectAggregator能够
                 * 将HTTPMessage和HTTPContent
                 * 合并为FullHttpRequest或者FullHttpResponse
                 *
                 * *********************/

                //处理HTTP 的POST请求
                .addLast(new HttpObjectAggregator(65536))

                //对WebSocket协议解析
                .addLast(new WebSocketServerProtocolHandler(
                        "/ws"))

                /* **********************
                 *
                 * 自定义处理器
                 *
                 * *********************/

                //添加处理器
                .addLast(new ImoocWebSocketInboundHandler())

        ;
    }
}
