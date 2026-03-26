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
     * 发送消息给指定用户
     */
    private void sendToUser(Long userId, Message message) {
        Channel channel = ChannelManageUtil.getChannel(userId);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
            log.info("消息已发送给用户: {}", userId);
        } else {
            log.warn("用户 {} 的Channel不存在或未激活", userId);
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
