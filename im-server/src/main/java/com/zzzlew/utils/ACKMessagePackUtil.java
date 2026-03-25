package com.zzzlew.utils;

import com.zzzlew.domain.response.ACKMessageResponseVO;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 21:14
 * @Description: com.zzzlew.utils
 * @version: 1.0
 */
public class ACKMessagePackUtil {

    /**
     * 创建成功ACK消息
     *
     * @param messageId 消息ID
     * @return ACK消息对象
     */
    public static ACKMessageResponseVO createSuccessACK(String tempId, String messageId) {
        ACKMessageResponseVO ack = new ACKMessageResponseVO();
        ack.setTempId(tempId);
        ack.setMessageId(messageId);
        ack.setStatus(0); // 0表示成功
        ack.setErrorCase(null);
        return ack;
    }

    /**
     * 创建失败ACK消息
     *
     * @param messageId 消息ID
     * @param errorCase 错误原因
     * @return ACK消息对象
     */
    public static ACKMessageResponseVO createFailedACK(String tempId, String messageId, String errorCase) {
        ACKMessageResponseVO ack = new ACKMessageResponseVO();
        ack.setTempId(tempId);
        ack.setMessageId(messageId);
        ack.setStatus(1); // 1表示失败
        ack.setErrorCase(errorCase);
        return ack;
    }
}
