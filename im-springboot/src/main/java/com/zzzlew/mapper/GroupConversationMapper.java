package com.zzzlew.mapper;

import com.zzzlew.pojo.dto.conversation.GroupConversationDTO;
import com.zzzlew.pojo.dto.user.GroupMemberDTO;
import com.zzzlew.pojo.entity.GroupConversation;
import com.zzzlew.pojo.vo.conversation.ConversationVO;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/23 - 11 - 23 - 11:45
 * @Description: com.zzzlew.zzzimserver.mapper
 * @version: 1.0
 */
public interface GroupConversationMapper {

    /**
     * 插入群聊会话
     *
     * @param groupConversationDTO 群聊会话信息
     */
    void insertGroupConversation(GroupConversationDTO groupConversationDTO);

    /**
     * 插入群成员
     *
     * @param groupMemberDTO 群成员信息
     */
    void insertGroupMember(GroupMemberDTO groupMemberDTO);

    /**
     * 更新群聊会话的群成员数量
     *
     * @param conversationId 群聊会话ID
     * @param groupAvatar 群聊会话头像
     */
    void updateGroupConversation(String conversationId, String groupAvatar);

    /**
     * 根据群聊会话ID列表查询群聊会话列表
     *
     * @param groupIdList 群聊会话ID列表
     * @return 群聊会话列表
     */
    List<GroupConversation> selectGroupConversationListByConversationIdList(List<String> groupIdList);

    /**
     * 根据群id查群群会话信息
     *
     * @param conversationId 群聊会话ID
     * @return 群聊会话信息
     */
    ConversationVO selectGroupConversation(String conversationId);

    /**
     * 根据群id查询群成员id列表
     *
     * @param conversationId 群聊会话ID
     * @return 群成员id集合
     */
    List<String> selectGroupNumber(String conversationId);
}
