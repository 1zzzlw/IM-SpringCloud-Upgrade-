package com.zzzlew.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/20 - 11 - 20 - 0:19
 * @Description: com.zzzlew.zzzimserver.handler
 * @version: 1.0
 */

@Slf4j
@ChannelHandler.Sharable
public class ByteBufToBinaryWebSocketFrameHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf byteBuf) {
            log.info("将ByteBuf包装为BinaryWebSocketFrame，内容长度: {}", byteBuf.readableBytes());
            // 将ByteBuf包装为BinaryWebSocketFrame
            BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
            // 继续传递包装后的消息
            ctx.write(frame, promise);
        } else {
            // 非ByteBuf类型的消息直接传递
            super.write(ctx, msg, promise);
        }
    }
}