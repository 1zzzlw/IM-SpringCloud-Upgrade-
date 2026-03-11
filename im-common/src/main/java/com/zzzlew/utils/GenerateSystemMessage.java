package com.zzzlew.utils;

import org.springframework.stereotype.Component;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/11 - 03 - 11 - 12:06
 * @Description: com.zzzlew.utils
 * @version: 1.0
 */
@Component
public class GenerateSystemMessage {
    /**
     * 生成系统消息内容
     *
     * @param type          消息类型
     * @param operatorId    操作者ID
     * @param currentUserId 当前用户ID
     * @return 系统消息内容
     */
    public String generateContent(Integer type, Long operatorId, Long currentUserId) {
        boolean isSelf = operatorId.equals(currentUserId);
        switch (type) {
            case 1 -> {
                return isSelf ? "你撤回了一条消息" : "对方撤回了一条消息";
            }
            default -> {
                return null;
            }
        }
    }
}
