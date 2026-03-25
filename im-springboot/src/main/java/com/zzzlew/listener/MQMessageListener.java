package com.zzzlew.listener;

import com.zzzlew.domain.ClusterMessageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import static com.zzzlew.constant.RabbitMQConstant.QUEUE_STORGE_PREFIX;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 23:07
 * @Description: com.zzzlew.listener
 * @version: 1.0
 */
@Slf4j
@Configuration
public class MQMessageListener {

    /**
     * 监听本集群的消息队列
     */
    @RabbitListener(queues = QUEUE_STORGE_PREFIX)
    public void handleClusterMessage(ClusterMessageWrapper wrapper) {
        log.info("收到集群消息: {}", wrapper.getMessage());
        // TODO 监听到消息信息存储到数据库中

    }

}
