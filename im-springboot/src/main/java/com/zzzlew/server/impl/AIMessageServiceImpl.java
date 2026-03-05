package com.zzzlew.server.impl;

import cn.hutool.core.util.IdUtil;
import com.zzzlew.mapper.AIMessageMapper;
import com.zzzlew.pojo.dto.message.AIMessageDTO;
import com.zzzlew.pojo.vo.message.AIMessageVO;
import com.zzzlew.pojo.vo.user.AIPersonalityVO;
import com.zzzlew.server.AIMessageService;
import com.zzzlew.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/5 - 03 - 05 - 13:51
 * @Description: com.zzzlew.server.impl
 * @version: 1.0
 */
@Slf4j
@Service
public class AIMessageServiceImpl implements AIMessageService {

    @Resource
    private ChatClient OllamaChatClient;
    @Resource
    private AIMessageMapper aiMessageMapper;

    @Override
    public List<AIMessageVO> loadMessage() {
        // 获得当前用户的id
        Long userId = UserHolder.getUser().getId();
        List<AIMessageVO> history = aiMessageMapper.selectHistory(userId);
        return history;
    }

    @Override
    public Flux<String> sendMessage(AIMessageDTO aiMessageDTO) {
        ChatClient.ChatClientRequestSpec request = OllamaChatClient.prompt();

        // 获得用户发送的内容
        String content = aiMessageDTO.getContent();
        StringBuilder fullContent = new StringBuilder();
        aiMessageDTO.setId(IdUtil.getSnowflakeNextId());


        // 获得历史消息
        List<AIMessageVO> history = aiMessageMapper.selectHistory(aiMessageDTO.getUserId());

        log.info("历史消息为: {}", history);
        List<Message> historyMessages = new ArrayList<>();
        for (AIMessageVO msg : history) {
            if ("user".equals(msg.getRole())) {
                historyMessages.add(new UserMessage(msg.getContent()));
            } else {
                historyMessages.add(new AssistantMessage(msg.getContent()));
            }
        }

        // 返回ai思考的消息，并保存到数据库
        return OllamaChatClient.prompt().messages(historyMessages).user(content).stream().content().doOnNext(chunk -> {
            // 每个chunk追加到完整内容
            fullContent.append(chunk);
        }).doOnComplete(() -> {
            // 流结束后保存到数据库
            log.info("保存消息到数据库：{}", fullContent);
            AIMessageDTO aiResponseDTO = new AIMessageDTO();
            aiResponseDTO.setId(IdUtil.getSnowflakeNextId());
            aiResponseDTO.setUserId(aiMessageDTO.getUserId());
            aiResponseDTO.setRole("assistant");
            aiResponseDTO.setMsgType(1);
            aiResponseDTO.setContent(fullContent.toString());
            aiMessageMapper.saveMessage(aiResponseDTO);
            // 保存用户发送的消息
            aiMessageMapper.saveMessage(aiMessageDTO);
        });
    }

    @Override
    public List<AIPersonalityVO> listPersonality() {
        log.info("获取ai个性化配置列表");
        // 获得当前用户的id
        Long userId = UserHolder.getUser().getId();
        List<AIPersonalityVO> personality = aiMessageMapper.getPersonality(userId);
        return personality;
    }
}
