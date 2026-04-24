package com.zzzlew.listener;

import com.zzzlew.domain.ClusterMessageWrapper;
import com.zzzlew.domain.Message;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 16:12
 * @Description: com.zzzlew.listener
 * @version: 1.0
 */
@Slf4j
@Configuration
public class MQMessageListener {
    /**
     * 监听本集群的消息队列
     */
    @RabbitListener(queues = "#{NettyConfig.getClusterQueueName()}")
    public void handleClusterMessage(ClusterMessageWrapper<Message> wrapper) {
        log.info("收到集群消息: {}", wrapper);

        Message message = wrapper.getMessage();
        Long targetUserId = wrapper.getTargetUserId();

        sendToUser(targetUserId, message);
    }

    /**
     * 发送消息给指定用户（同步等待，带重试机制）
     */
    private void sendToUser(Long userId, Message message) {
        Channel channel = ChannelManageUtil.getChannel(userId);
        if (channel == null || !channel.isActive()) {
            log.warn("用户 {} 的Channel不存在或未激活", userId);
            throw new RuntimeException("Channel不可用，触发MQ重试");
        }

        // 检查Channel是否可写
        if (!channel.isWritable()) {
            log.warn("用户 {} 的Channel写缓冲区已满", userId);
            throw new RuntimeException("Channel不可写，触发MQ重试");
        }

        try {
            // 同步等待发送完成（最多等待3秒）
            io.netty.channel.ChannelFuture future = channel.writeAndFlush(message);
            boolean success = future.await(3000);

            if (!success) {
                log.error("用户 {} 消息发送超时(3秒)", userId);
                throw new RuntimeException("发送超时，触发MQ重试");
            }

            if (!future.isSuccess()) {
                log.error("用户 {} 消息发送失败: {}", userId, future.cause().getMessage());
                throw new RuntimeException("发送失败，触发MQ重试", future.cause());
            }

            log.info("消息已发送给用户: {}", userId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("用户 {} 消息发送被中断", userId, e);
            throw new RuntimeException("发送被中断，触发MQ重试", e);
        }
    }

    /**
     * 广播消息给本集群所有用户
     */
    private void broadcastToAllUsers(Message message) {
        // 获取本集群所有在线用户的Channel并发送消息
        // 这里需要根据你的ChannelManageUtil实现来获取所有用户
        log.info("广播消息给本集群所有用户: {}", message);
    }

}
