package com.zzzlew.pojo.vo.conversation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/22 - 11 - 22 - 21:45
 * @Description: com.zzzlew.zzzimserver.pojo.vo.conversation
 * @version: 1.0
 */
@Data
public class GroupConversationVO {

    /**
     * 群聊ID
     */
    private String id;

    /**
     * 群聊名称
     */
    private String groupName;

    /**
     * 群聊头像
     */
    private String groupAvatar;

    /**
     * 群主ID
     */
    private Long ownerId;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 最新消息
     */
    private String latestMsg;

    /**
     * 最后一条消息时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestMsgTime;

    /**
     * 未读消息数量
     */
    private Integer unreadCount;

    /**
     * 会话状态
     */
    private Integer status;
}
