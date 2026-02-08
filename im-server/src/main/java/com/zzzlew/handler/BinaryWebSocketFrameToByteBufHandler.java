package com.zzzlew.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 23:35
 * @Description: com.zzzlew.zzzimserver.handler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class BinaryWebSocketFrameToByteBufHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果是BinaryWebSocketFrame，提取其中的内容
        if (msg instanceof BinaryWebSocketFrame frame) {
            log.info("开始处理BinaryWebSocketFrame，内容长度为：{}", frame.content().readableBytes());

            // 提取frame内容
            ByteBuf content = frame.content();

            if (content.readableBytes() >= 4) {
                int magicNum = content.getInt(content.readerIndex());
                log.info("消息魔数: {}", magicNum);
            }

            // 将frame内容传递给下一个处理器
            ctx.fireChannelRead(frame.content());
        } else {
            // 其他类型的消息直接传递
            super.channelRead(ctx, msg);
        }
    }
}
