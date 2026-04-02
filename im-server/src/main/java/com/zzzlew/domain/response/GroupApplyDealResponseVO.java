package com.zzzlew.domain.response;

import com.zzzlew.domain.Message;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/17 - 03 - 17 - 17:49
 * @Description: com.zzzlew.domain.response
 * @version: 1.0
 */
@Data
public class GroupApplyDealResponseVO extends Message implements Serializable {
    /**
     * 群聊id
     */
    private String conversationId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色 0：普通用户 1：管理员 2-群主
     */
    private Integer role;

    @Override
    public int getMessageType() {
        return GroupApplyDealResponseVO;
    }
}
