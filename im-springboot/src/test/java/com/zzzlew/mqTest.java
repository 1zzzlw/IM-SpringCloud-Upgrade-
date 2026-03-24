package com.zzzlew;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 12:12
 * @Description: com.zzzlew
 * @version: 1.0
 */
@SpringBootTest
public class mqTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void springbootSend() {
        String exchangeName = "im-topic-exchange";
        String message = "你好";

        rabbitTemplate.convertAndSend(exchangeName, "im.storage", message);
    }
}
