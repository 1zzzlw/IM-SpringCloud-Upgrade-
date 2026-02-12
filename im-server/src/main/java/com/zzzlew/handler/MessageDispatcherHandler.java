package com.zzzlew.handler;


import cn.hutool.extra.spring.SpringUtil;
import com.zzzlew.constant.RedisConstant;
import com.zzzlew.domain.Message;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.handler.messageHandler.ApplySendHandler;
import com.zzzlew.handler.messageHandler.GroupApplySendHandler;
import com.zzzlew.handler.messageHandler.GroupChatHandler;
import com.zzzlew.handler.messageHandler.PrivateChatHandler;
import com.zzzlew.result.MessageResult;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.zzzlew.constant.RedisConstant.*;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 21:00
 * @Description: com.zzzlew.zzzimserver.handler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageDispatcherHandler extends ChannelInboundHandlerAdapter {

    private RedissonClient redissonClient;
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        if (this.redissonClient == null && this.stringRedisTemplate == null) {
            this.redissonClient = SpringUtil.getBean(RedissonClient.class);
            this.stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        }
    }

    // 存放 消息类型 和 Handler 的映射，方便分发给对应的 Handler 处理
    private final Map<Integer, MessageHandler> handlerMap;

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
            if (message.getMessageType() == 0) {
                // 心跳消息
                Long userId = ChannelManageUtil.getUser(ctx.channel()).getId();
                handleHeartMessage(userId);
                return;
            }
            // 根据消息类型，分发给对应的 Handler 处理
            MessageHandler handler = handlerMap.get(message.getMessageType());
            if (handler != null) {
                log.info("分发给 Handler: {}", handler.getClass().getSimpleName());
                MessageResult result = handler.handle(ctx, message);
                RTopic topic = redissonClient.getTopic(SYSTEM_MESSAGE_BROADCAST);
                // 广播消息
                topic.publish(result);
            } else {
                log.warn("未找到对应的 Handler 处理消息类型: {}", message.getMessageType());
            }
        }
        // 其他类型的消息，直接传递给下一个 Handler 处理，其实就是ctx.fireChannelRead(msg);
        super.channelRead(ctx, msg);
    }

    public void handleHeartMessage(Long userId) {
        log.info("接收心跳消息，更新redis缓存");
        // 更新好友信息的过期时间
        String friendListKey = USER_FRIEND_LIST_KEY + userId;
        // 重置过期时间
        stringRedisTemplate.expire(friendListKey, USER_FRIEND_LIST_KEY_TTL, TimeUnit.MINUTES);
        // 更新在线过期时间
        stringRedisTemplate.expire(RedisConstant.USER_ONLINE_STATUS_KEY, RedisConstant.USER_ONLINE_STATUS_KEY_TTL, TimeUnit.SECONDS);
    }
}
