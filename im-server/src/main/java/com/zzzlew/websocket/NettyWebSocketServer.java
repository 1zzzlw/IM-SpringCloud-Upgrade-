package com.zzzlew.websocket;


import com.zzzlew.handler.*;
import com.zzzlew.protocol.MessageCodecSharable;
import com.zzzlew.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/17 - 11 - 17 - 20:46
 * @Description: com.zzzlew.zzzimserver.websocket
 * @version: 1.0
 */
@Slf4j
@Service
public class NettyWebSocketServer {

    @Value("${netty.websocket.port}")
    private Integer port;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());

    // 启动Netty服务器
    @PostConstruct
    public void start() throws InterruptedException {
        run();
        log.info("Netty服务器启动成功，端口：{}", port);
    }

    // 结束前销毁Netty服务器
    @PreDestroy
    public void destroy() {
        Future<?> future1 = bossGroup.shutdownGracefully();
        Future<?> future2 = workerGroup.shutdownGracefully();
        future1.syncUninterruptibly();
        future2.syncUninterruptibly();
        log.info("Netty服务器销毁成功");
    }

    public void run() throws InterruptedException {
        // 创建Netty服务器启动器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                // 用于设置 TCP 连接的 半连接队列（backlog）大小
                .option(ChannelOption.SO_BACKLOG, 128)
                // 用于设置 TCP 连接的 保活时间（keep-alive），单位为秒
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 用于记录 Netty 服务器的日志
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                        pipeline.addLast(new HttpServerCodec());
                        // 将HTTP消息的多个部分聚合成完整的FullHttpRequest 确保收到完整的HTTP请求再处理 ，避免处理不完整的请求
                        // 入站请求，这个处理器只在入站的时候处理
                        pipeline.addLast(new HttpObjectAggregator(128 * 1024));
                        // 出战请求，用于处理 HTTP 消息的分块写入，确保数据按块发送 这个处理器在消息入站的时候不使用，因为消息入站的只是请求
                        pipeline.addLast(new ChunkedWriteHandler());
                        // 自定义 Http 处理头，用于处理 Http 头信息
                        pipeline.addLast(new HttpHeadersHandler());
                        // 用于处理 WebSocket 协议的握手、升级和关闭，会自动处理 ping/pong 帧
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 131072, true, true, 10000L));
                        // 用于将 BinaryWebSocketFrame 转换为 ByteBuf ，因为后续的处理器都需要 ByteBuf 类型的消息
                        pipeline.addLast(new BinaryWebSocketFrameToByteBufHandler());
                        // 自定义协议处理，WebSocket 升级完成后使用
                        pipeline.addLast(new ProtocolFrameDecoder());
                        // 出站转换处理器 - 必须放在所有出站消息处理之后，但在WebSocketServerProtocolHandler之前
                        pipeline.addLast(new ByteBufToBinaryWebSocketFrameHandler());
                        // 消息编码器，用于将消息编码为 ByteBuf ，因为后续的处理器都需要 ByteBuf 类型的消息
                        pipeline.addLast(new MessageCodecSharable());
                        // 消息分发处理器，用于分发消息到不同的处理器
                        pipeline.addLast(new MessageDispatcherHandler());
                        // 30 秒没有读写操作时触发 IdleState.READER_IDLE 事件
                        // 这个心跳处理器因为只有读时间，所以只在入站的时候处理，出战的时候虽然经过但是没有处理业务
                        pipeline.addLast(new IdleStateHandler(90, 0, 0, TimeUnit.SECONDS));
                        // 心跳处理器，用于处理心跳包
                        pipeline.addLast(new HeartBeatHandler());
                        // 连接成功处理器，用于处理连接成功事件，此时用户上线
                        pipeline.addLast(new ConnectSuccessMessageHandler());
                        // 退出登录处理器，用于处理退出登录请求
                        pipeline.addLast(new QuitLoginHandler());
                    }
                });
        // 启动Netty服务器
        String wsPortStr = System.getProperty("ws.port");
        if (!StringUtil.isBlank(wsPortStr)) {
            port = Integer.parseInt(wsPortStr);
        }
        serverBootstrap.bind(port).sync();
    }

}
