package com.zzzlew.config;

import com.zzzlew.tools.AITools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
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
        ToolCallback[] tools = ToolCallbacks.from(new AITools());

        return ChatClient.builder(ollamaChatModel).defaultToolCallbacks(tools)
                .defaultSystem("""
                        你是用户的ai聊天好友。
                        遵循规则：
                            1. 历史消息仅供参考，只回答用户的最新消息
                            2. 回答简短（1-3句话），口语化，像朋友聊天
                            3. 不要加括号说明，不要重复相同的开头
                        个性化信息设置：
                            - 你会收到角色设定，严格按照设定的性格和语气回答
                            - 保持角色一致，自然灵活
                        工具使用：
                            - 当需要准确数据时，优先使用工具而不是猜测
                        请自然地回答用户的最新消息。
                        """)
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
