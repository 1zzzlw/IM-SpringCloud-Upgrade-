package com.zzzlew.handler.impl;

import com.zzzlew.domain.Message;
import com.zzzlew.result.MessageResult;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: zzzlew
 * @Date: 2026/2/8 - 02 - 08 - 22:07
 * @Description: com.zzzlew.handler.impl
 * @version: 1.0
 */
public interface MessageHandler<T extends Message> {
    MessageResult handle(ChannelHandlerContext ctx, T request);
}
