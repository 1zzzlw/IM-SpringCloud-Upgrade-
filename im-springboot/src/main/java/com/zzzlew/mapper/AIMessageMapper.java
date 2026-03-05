package com.zzzlew.mapper;


import com.zzzlew.pojo.dto.message.AIMessageDTO;
import com.zzzlew.pojo.vo.message.AIMessageVO;
import com.zzzlew.pojo.vo.user.AIPersonalityVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/5 - 03 - 05 - 14:02
 * @Description: com.zzzlew.mapper
 * @version: 1.0
 */
public interface AIMessageMapper {

    /**
     * 获取历史消息
     *
     * @param userId
     * @return
     */
    List<AIMessageVO> selectHistory(Long userId);

    /**
     * 保存消息
     *
     * @param aiMessageDTO
     */
    void saveMessage(AIMessageDTO aiMessageDTO);

    /**
     * 获取用户个性化列表
     *
     * @return
     */
    List<AIPersonalityVO> getPersonality(Long userId);

}
