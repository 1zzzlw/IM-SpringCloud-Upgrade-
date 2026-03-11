package com.zzzlew.pojo.vo.message;

import lombok.Data;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/11 - 03 - 11 - 11:25
 * @Description: com.zzzlew.pojo.vo.message
 * @version: 1.0
 */
@Data
public class SystemMessageVO {
    /**
     * 消息id
     */
    private Long id;

    /**
     * 接收者id
     */
    private String receiverId;

    /**
     * 会话id
     */
    private String conversationId;

    /**
     * 发送者id
     */
    private Long senderId;

    /**
     * 消息类型 1：文本消息 2：图片消息 3：语音消息 4：视频消息 5：文件消息
     */
    private Integer msgType;

    /**
     * 系统消息子类型
     */
    private Integer subType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送状态 0 -发送中 1 -成功 2 -失败
     */
    private Integer sendStatus;
}
