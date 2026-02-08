package com.zzzlew.domain.response;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/22 - 11 - 22 - 17:27
 * @Description: com.zzzlew.zzzimserver.pojo.vo.apply
 * @version: 1.0
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class GroupApplyResponseVO extends Message implements Serializable {

    /**
     * 群会话ID
     */
    private String conversationId;

    /**
     * 群主ID
     */
    private Long userId;

    /**
     * 群主头像
     */
    private String userAvatar;

    /**
     * 群聊名称
     */
    private String groupName;

    /**
     * 申请状态
     */
    private Integer status;

    @Override
    public int getMessageType() {
        return GroupApplyResponseVO;
    }
}
