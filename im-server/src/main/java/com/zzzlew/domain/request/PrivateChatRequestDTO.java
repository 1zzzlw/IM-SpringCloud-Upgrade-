package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 16:12
 * @Description: com.zzzlew.zzzimserver.pojo.dto.message
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateChatRequestDTO extends Message implements Serializable {

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

    /**
     * 发送状态 0 -发送中 1 -成功 2 -失败
     */
    private Integer sendStatus;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 存储桶名称
     */
    private String bucket;

    /**
     * 远程文件路径
     */
    private String remotePath;

    /**
     * 本地文件路径
     */
    private String localPath;

    /**
     * 远程文件路径
     */
    private String remoteUrl;

    /**
     * 预览base64
     */
    private String previewBase64;

    @Override
    public int getMessageType() {
        return PrivateChatRequestDTO;
    }

}
