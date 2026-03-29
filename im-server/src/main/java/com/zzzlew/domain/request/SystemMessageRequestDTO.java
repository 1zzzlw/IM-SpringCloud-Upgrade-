package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

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
     * 系统消息ID
     */
    private Long id;

    /**
     * 操作者ID (谁发起的：如群主、撤回者)
     */
    private Long senderId;

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 接收者ID (群ID或对方用户ID)
     */
    private Long receiverId;

    /**
     * 群聊的接收者ID
     */
    private List<Long> receiverIds;

    /**
     * 系统消息固定为99
     */
    private Integer msgType = 99;

    /**
     * 系统消息子类型
     */
    private Integer subType;

    /**
     * 结构：{"opId":1, "opName":"张三", "trId":2, "trName":"李四", "ext":{}}
     */
    private String content;

    @Override
    public int getMessageType() {
        return SystemMessageRequestDTO;
    }
}
