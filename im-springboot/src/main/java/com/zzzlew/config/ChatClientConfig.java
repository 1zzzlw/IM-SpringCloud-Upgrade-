package com.zzzlew.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/5 - 03 - 05 - 13:36
 * @Description: com.zzzlew.config
 * @version: 1.0
 */
@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient OllamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("你是一个友好的AI助手")
                .build();
    }

    // @Bean
    // public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
    //     return ChatClient.builder(ollamaChatModel)
    //             // 1. 默认系统提示词
    //             .defaultSystem("你是一个友好的AI助手")
    //
    //             // 2. 默认用户提示词（少用）
    //             .defaultUser("请用中文回答")
    //
    //             // 3. 默认函数（Function Calling）
    //             .defaultFunctions("weatherFunction", "searchFunction")
    //
    //             // 4. 默认选项（温度、最大token等）
    //             .defaultOptions(OllamaOptions.create()
    //                     .withModel("qwen2:1.5b")
    //                     .withTemperature(0.7)
    //                     .withMaxTokens(2000))
    //
    //             // 5. 默认Advisor（拦截器，用于日志、监控等）
    //             .defaultAdvisors(new LoggingAdvisor())
    //
    //             .build();
    // }

}
