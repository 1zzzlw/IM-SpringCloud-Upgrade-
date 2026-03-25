package com.zzzlew.config;

import cn.hutool.extra.spring.SpringUtil;
import jodd.util.StringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zzzlew.constant.RabbitMQConstant.QUEUE_NETTY_PREFIX;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 16:43
 * @Description: com.zzzlew.config
 * @version: 1.0
 */
@Configuration("NettyConfig")
public class NettyConfig {

    public static String NETTY_CLUSTER_ID;
    public static Integer NETTY_PORT;

    public NettyConfig() {
        // 1. 优先读取 JVM 启动参数：-Dws.mq=1
        String NettyMQId = System.getProperty("ws.mq");
        String wsPortStr = System.getProperty("ws.port");

        if (!StringUtil.isBlank(NettyMQId)) {
            NETTY_CLUSTER_ID = NettyMQId;
        } else {
            // 2. 读取配置文件
            NETTY_CLUSTER_ID = SpringUtil.getProperty("netty.websocket.cluster-id");
        }

        if (!StringUtil.isBlank(wsPortStr)) {
            NETTY_PORT = Integer.parseInt(wsPortStr);
        } else {
            NETTY_PORT = Integer.parseInt(SpringUtil.getProperty("netty.websocket.port"));
        }
    }

    @Bean
    public String getClusterQueueName() {
        return String.format("%s%s", QUEUE_NETTY_PREFIX, NETTY_CLUSTER_ID);
    }
}
