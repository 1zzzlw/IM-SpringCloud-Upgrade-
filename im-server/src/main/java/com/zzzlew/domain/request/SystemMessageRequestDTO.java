package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/10 - 03 - 10 - 21:09
 * @Description: com.zzzlew.domain.request
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemMessageRequestDTO extends Message implements Serializable {

    /**
     * 消息id
     */
    private Long id;

    /**
     * 发送者id
     */
    private Long senderId;

    /**
     * 会话id
     */
    private String conversationId;

    /**
     * 接收者id
     */
    private Long receiverId;

    /**
     * 消息类型 1：文本消息 2：图片消息 3：语音消息 4：视频消息 5：文件消息
     */
    private Integer msgType;

    /**
     * 消息内容
     */
    private String content;

    @Override
    public int getMessageType() {
        return SystemMessageRequestDTO;
    }
}
