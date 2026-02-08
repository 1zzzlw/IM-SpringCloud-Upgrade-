package com.zzzlew.server;

import com.zzzlew.pojo.vo.conversation.ConversationVO;
import com.zzzlew.pojo.vo.user.GroupMemberVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/21 - 11 - 21 - 21:40
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface ConversationService {
    /**
     * 全量更新并初始化会话列表
     *
     * @param isInit 是否初始化
     * @return 会话列表
     */
    List<ConversationVO> initConversationList(Boolean isInit);

    /**
     * 获取群成员列表
     *
     * @param conversationId 会话id
     * @return 群成员列表
     */
    List<GroupMemberVO> getGroupMemberList(String conversationId);

    /**
     * 清空会话未读消息数量
     *
     * @param conversationId 会话id
     */
    void clearConversationUnreadCounts(String conversationId);

}
