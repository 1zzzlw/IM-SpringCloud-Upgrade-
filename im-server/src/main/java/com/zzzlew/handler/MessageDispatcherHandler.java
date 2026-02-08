package com.zzzlew.handler;


import com.zzzlew.domain.Message;
import com.zzzlew.handler.messageHandler.ApplySendHandler;
import com.zzzlew.handler.messageHandler.GroupApplySendHandler;
import com.zzzlew.handler.messageHandler.GroupChatHandler;
import com.zzzlew.handler.messageHandler.PrivateChatHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 21:00
 * @Description: com.zzzlew.zzzimserver.handler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageDispatcherHandler extends ChannelInboundHandlerAdapter {

    // 存放 消息类型 和 Handler 的映射，方便分发给对应的 Handler 处理
    private final Map<Integer, ChannelInboundHandler> handlerMap;

    public MessageDispatcherHandler() {
        handlerMap = new ConcurrentHashMap<>();
        // 注册消息类型与对应的 Handler
        handlerMap.put(Message.PrivateChatRequestDTO, new PrivateChatHandler());
        handlerMap.put(Message.GroupChatRequestDTO, new GroupChatHandler());
        handlerMap.put(Message.ApplyRequestDTO, new ApplySendHandler());
        handlerMap.put(Message.GroupApplyRequestDTO, new GroupApplySendHandler());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 自定义的消息编码器会将消息转换为 Message 对象
        if (msg instanceof Message message) {
            // 打印消息类型
            log.info("收到消息类型: {}", message.getMessageType());
            // 根据消息类型，分发给对应的 Handler 处理
            ChannelInboundHandler handler = handlerMap.get(message.getMessageType());
            if (handler != null) {
                log.info("分发给 Handler: {}", handler.getClass().getSimpleName());
                handler.channelRead(ctx, message);
            } else {
                log.warn("未找到对应的 Handler 处理消息类型: {}", message.getMessageType());
            }
        }
        // 其他类型的消息，直接传递给下一个 Handler 处理，其实就是ctx.fireChannelRead(msg);
        super.channelRead(ctx, msg);
    }
}
