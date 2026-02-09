package com.zzzlew.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.zzzlew.constant.RedisConstant;
import com.zzzlew.domain.response.LoginSuccessMessage;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zzzlew.constant.RedisConstant.USER_FRIEND_LIST_KEY;


/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 15:15
 * @Description: com.zzzlew.zzzimserver.websocket
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ConnectSuccessMessageHandler extends ChannelInboundHandlerAdapter {

    private StringRedisTemplate stringRedisTemplate;

    // 需要手动注入 StringRedisTemplate，不能使用 @Autowired 注解，因为 NettyWebSocketServerHandler 是手动创建的，
    // 而不是由 Spring 管理的，并且 handler不能注入到spring 容器中，所以需要手动注入 StringRedisTemplate
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (stringRedisTemplate == null) {
            stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        }
    }

    /**
     * 通道就绪后调用，一般用来初始化
     *
     * @param ctx 通道处理器上下文（包含当前Channel信息）
     * @throws Exception 处理过程中的异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端 {} 建立连接，通道开启！", ctx.channel().remoteAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("ws协议正式搭建完成，通道成功开启，客户端 {} 握手完成！", ctx.channel().remoteAddress());
            // TODO 这里的操作基本上是操作的redis，以后的优化需要在把这里的操作通过Dubbo通信调用websocket服务来分离Netty通信层和springboot业务层
            // 通知其他用户当前用户上线
            Long userId = ChannelManageUtil.getUser(ctx.channel()).getId();
            String key = USER_FRIEND_LIST_KEY + userId;
            // 1 从redis中获取当前用户的好友列表id
            List<String> friendIds = stringRedisTemplate.opsForSet().members(key).stream().toList();
            for (String friendId : friendIds) {
                Channel channel = ChannelManageUtil.getChannel(Long.parseLong(friendId));
                if (channel != null) {
                    channel.writeAndFlush(new LoginSuccessMessage());
                } else {
                    log.info("用户 {} 不在线", friendId);
                }
            }
            String onlineStatusKey = RedisConstant.USER_ONLINE_STATUS_KEY + userId;
            // 修改redis中的用户状态为在线
            stringRedisTemplate.opsForSet().add(onlineStatusKey, "online");
            // 设置过期时间为270秒，因为用户在270秒内没有操作，就可以判断为用户不在线，对应心跳时间90秒
            stringRedisTemplate.expire(onlineStatusKey, RedisConstant.USER_ONLINE_STATUS_KEY_TTL, TimeUnit.SECONDS);
        }
    }

}
