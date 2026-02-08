package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/22 - 11 - 22 - 17:28
 * @Description: com.zzzlew.zzzimserver.pojo.dto.apply
 * @version: 1.0
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class GroupApplyRequestDTO extends Message implements Serializable {

    /**
     * 群会话ID
     */
    private String conversationId;

    /**
     * 群主头像
     */
    private String userAvatar;

    /**
     * 群聊名称
     */
    private String groupName;

    /**
     * 被邀请的用户ID列表，逗号分隔
     */
    private String invitedIds;

    @Override
    public int getMessageType() {
        return GroupApplyRequestDTO;
    }

}
