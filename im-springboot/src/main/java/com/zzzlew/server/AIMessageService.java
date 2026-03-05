package com.zzzlew.server;

import com.zzzlew.pojo.dto.message.AIMessageDTO;
import com.zzzlew.pojo.vo.message.AIMessageVO;
import com.zzzlew.pojo.vo.user.AIPersonalityVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/5 - 03 - 05 - 13:51
 * @Description: com.zzzlew.server
 * @version: 1.0
 */
public interface AIMessageService {

    /**
     * 加载历史消息
     *
     * @return
     */
    List<AIMessageVO> loadMessage();

    /**
     * 发送消息
     *
     * @param aiMessageDTO
     * @return
     */
    Flux<String> sendMessage(AIMessageDTO aiMessageDTO);


    /**
     * 获取ai个性化配置列表
     *
     * @return
     */
    List<AIPersonalityVO> listPersonality();

}
