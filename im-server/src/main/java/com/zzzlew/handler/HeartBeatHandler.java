package com.zzzlew.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 14:22
 * @Description: com.zzzlew.zzzimserver.websocket
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
// 继承ChannelDuplexHandler，可以处理入站和出站事件，来处理心跳包
public class HeartBeatHandler extends ChannelDuplexHandler {

    // 处理非IO事件的入口，事件会通过userEventTriggered方法传递给处理器
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent e) {
            // 处理心跳事件
            if (e.state() == IdleState.READER_IDLE) {
                // 处理读空闲事件
                log.info("心跳超时（90秒未读），关闭连接：{}", ctx.channel().remoteAddress());
                ctx.close();
            }
        }
        // 其他事件，继续传递
        ctx.fireUserEventTriggered(evt);
    }

}
