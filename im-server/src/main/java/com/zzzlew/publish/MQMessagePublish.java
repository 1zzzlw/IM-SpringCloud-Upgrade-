package com.zzzlew.publish;

import com.alibaba.fastjson.JSON;
import com.zzzlew.domain.ClusterMessageWrapper;
import com.zzzlew.domain.Message;
import com.zzzlew.domain.response.GroupChatResponseVO;
import com.zzzlew.domain.response.PrivateChatResponseVO;
import com.zzzlew.domain.response.SystemMessageResponseVO;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    // public void sendToCluster(MessageResult messageResult) {
    //     // 其中包含单聊和群聊的id集合
    //     List<Long> receiverIds = messageResult.getReceiverIds();
    //
    //     for (Long receiverId : receiverIds) {
    //         String nettyClusterKey = USER_CLUSTER_MAPPING_KEY + receiverId;
    //         // 获取接收者所在的集群ID
    //         String serverId = stringRedisTemplate.opsForValue().get(nettyClusterKey);
    //
    //         if (!StringUtils.isBlank(serverId)) {
    //             // 构建路由键
    //             String routingKey = QUEUE_NETTY_ROUTING_KEY + serverId;
    //             // 发送消息到指定Netty集群的消息队列
    //             rabbitTemplate.convertAndSend(EXCHANGE, routingKey, new ClusterMessageWrapper<Message>(messageResult.getResponse(), receiverId));
    //             log.info("消息已发送到集群: {} 接收者: {}", serverId, receiverId);
    //         } else {
    //             // 用户不在线或未找到集群信息
    //             log.info("用户: {} 不在线或未找到集群信息", receiverId);
    //             // 用户离线，记录离线消息标记到Redis
    //             recordOfflineMessageMarker(receiverId, messageResult.getResponse());
    //         }
    //     }
    //
    //     if (messageResult.getResponse() instanceof PrivateChatResponseVO || messageResult.getResponse() instanceof GroupChatResponseVO) {
    //         // 将消息发送到SpringBoot监听的消息队列中用来异步存储，如果用户不在线还可以直接从这里拉取离线消息
    //         rabbitTemplate.convertAndSend(EXCHANGE, QUEUE_STORGE_ROUTING_KEY, new ClusterMessageWrapper<Message>(messageResult.getResponse()));
    //     }
    // }

    /**
     * 发送消息到集群
     */
    public void sendToCluster(MessageResult messageResult) {
        List<Long> receiverIds = messageResult.getReceiverIds();
        if (receiverIds == null || receiverIds.isEmpty()) {
            return;
        }

        // --- 核心优化点：异步执行 ---
        // 使用 runAsync 将后续逻辑交给 Spring 默认线程池处理，Netty 线程此时可以立即释放去处理下一个请求
        CompletableFuture.runAsync(() -> {
            try {
                // 1. 批量获取 Redis 中的用户集群映射关系 (优化：从 N 次请求变为 1 次请求)
                List<String> keys = receiverIds.stream().map(id -> USER_CLUSTER_MAPPING_KEY + id).collect(Collectors.toList());
                List<String> serverIds = stringRedisTemplate.opsForValue().multiGet(keys);

                // 2. 遍历处理投递
                for (int i = 0; i < receiverIds.size(); i++) {
                    Long receiverId = receiverIds.get(i);
                    // 对应 Redis multiGet 返回的结果
                    String serverId = (serverIds != null && i < serverIds.size()) ? serverIds.get(i) : null;

                    if (!StringUtils.isBlank(serverId)) {
                        // 用户在线，投递到对应服务器的队列
                        String routingKey = QUEUE_NETTY_ROUTING_KEY + serverId;
                        // 这里沿用您现有的 ClusterMessageWrapper 结构，不需要改动其他类
                        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, new ClusterMessageWrapper<Message>(messageResult.getResponse(), receiverId));
                    } else if (messageResult.getResponse() instanceof PrivateChatResponseVO || messageResult.getResponse() instanceof GroupChatResponseVO || messageResult.getResponse() instanceof SystemMessageResponseVO) {
                        // 用户离线，记录标记
                        recordOfflineMessageMarker(receiverId, messageResult.getResponse());
                    }
                }

                // 3. 消息持久化异步存储 (发往 SpringBoot 消费端)
                if (messageResult.getResponse() instanceof PrivateChatResponseVO || messageResult.getResponse() instanceof GroupChatResponseVO || messageResult.getResponse() instanceof SystemMessageResponseVO) {
                    rabbitTemplate.convertAndSend(EXCHANGE, QUEUE_STORGE_ROUTING_KEY, new ClusterMessageWrapper<Message>(messageResult.getResponse()));
                }

                log.info("群聊消息异步投递完成，总目标数: {}", receiverIds.size());

            } catch (Exception e) {
                log.error("异步投递集群消息时发生异常", e);
            }
        });
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
