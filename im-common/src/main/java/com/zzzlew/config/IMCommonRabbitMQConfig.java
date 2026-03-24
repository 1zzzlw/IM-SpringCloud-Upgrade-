package com.zzzlew.config;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 11:02
 * @Description: com.zzzlew.config
 * @version: 1.0
 */
@Configuration
public class IMCommonRabbitMQConfig {

    // 定义共用的Topic交换机，所有模块共用
    @Bean(name = "imTopicExchange")
    public TopicExchange imTopicExchange() {
        return ExchangeBuilder.topicExchange("im-topic-exchange").durable(true).build();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

}
