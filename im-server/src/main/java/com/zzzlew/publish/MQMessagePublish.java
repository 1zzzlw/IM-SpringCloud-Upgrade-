package com.zzzlew.publish;

import com.alibaba.fastjson.JSON;
import com.zzzlew.domain.ClusterMessageWrapper;
import com.zzzlew.domain.Message;
import com.zzzlew.domain.response.GroupChatResponseVO;
import com.zzzlew.domain.response.PrivateChatResponseVO;
import com.zzzlew.result.MessageResult;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.zzzlew.constant.RabbitMQConstant.*;
import static com.zzzlew.constant.RedisConstant.*;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 16:07
 * @Description: com.zzzlew.publish
 * @version: 1.0
 */
@Slf4j
@Component
public class MQMessagePublish {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> storeOfflineMessageScript;

    static {
        // 提前读取lua文件，提高效率
        storeOfflineMessageScript = new DefaultRedisScript<>();
        // 设置lua脚本文件的位置
        storeOfflineMessageScript.setLocation(new ClassPathResource("store_offline_message.lua"));
        // 指定返回值
        storeOfflineMessageScript.setResultType(Long.class);
    }

    /**
     * 发送消息到集群
     * 智能路由：只发送到接收者所在的集群队列
     */
    public void sendToCluster(MessageResult messageResult) {
        // 其中包含单聊和群聊的id集合
        List<Long> receiverIds = messageResult.getReceiverIds();

        for (Long receiverId : receiverIds) {
            String nettyClusterKey = USER_CLUSTER_MAPPING_KEY + receiverId;
            // 获取接收者所在的集群ID
            String serverId = stringRedisTemplate.opsForValue().get(nettyClusterKey);

            if (!StringUtils.isBlank(serverId)) {
                // 构建路由键
                String routingKey = QUEUE_NETTY_ROUTING_KEY + serverId;
                // 发送消息到指定Netty集群的消息队列
                rabbitTemplate.convertAndSend(EXCHANGE, routingKey, new ClusterMessageWrapper<Message>(messageResult.getResponse(), receiverId));
                log.info("消息已发送到集群: {} 接收者: {}", serverId, receiverId);
            } else {
                // 用户不在线或未找到集群信息
                log.info("用户: {} 不在线或未找到集群信息", receiverId);
                // 用户离线，记录离线消息标记到Redis
                recordOfflineMessageMarker(receiverId, messageResult.getResponse());
            }
        }

        if (messageResult.getResponse() instanceof PrivateChatResponseVO || messageResult.getResponse() instanceof GroupChatResponseVO) {
            // 将消息发送到SpringBoot监听的消息队列中用来异步存储，如果用户不在线还可以直接从这里拉取离线消息
            rabbitTemplate.convertAndSend(EXCHANGE, QUEUE_STORGE_ROUTING_KEY, new ClusterMessageWrapper<Message>(messageResult.getResponse()));
        }
    }

    public void recordOfflineMessageMarker(Long userId, Message message) {
        String offlineMessageKey = USER_OFFLINE_MESSAGE_CONTENT_KEY + userId;
        String messageJson = JSON.toJSONString(message);
        long timestamp = System.currentTimeMillis();

        // 执行Lua脚本
        Long count = stringRedisTemplate.execute(storeOfflineMessageScript, Collections.singletonList(offlineMessageKey), messageJson, String.valueOf(timestamp), String.valueOf(USER_OFFLINE_MESSAGE_KEY_TTL));

        log.debug("用户 {} 离线消息已存储，当前消息数: {}", userId, count);
    }

    /**
     * 广播消息到所有集群
     */
    public void broadcastToAllClusters(MessageResult messageResult) {
    }

}
