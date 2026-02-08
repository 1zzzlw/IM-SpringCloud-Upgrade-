package com.zzzlew.server.impl;


import cn.hutool.core.util.IdUtil;
import com.zzzlew.mapper.ApplyMapper;
import com.zzzlew.mapper.ConversationMapper;
import com.zzzlew.mapper.FriendMapper;
import com.zzzlew.mapper.GroupConversationMapper;
import com.zzzlew.pojo.dto.apply.DealApplyDTO;
import com.zzzlew.pojo.dto.apply.DealGroupDTO;
import com.zzzlew.pojo.dto.apply.GroupApplyDTO;
import com.zzzlew.pojo.dto.apply.SendApplyDTO;
import com.zzzlew.pojo.dto.conversation.GroupConversationDTO;
import com.zzzlew.pojo.dto.user.GroupMemberDTO;
import com.zzzlew.pojo.vo.apply.ApplyVO;
import com.zzzlew.pojo.vo.apply.GroupApplyVO;
import com.zzzlew.pojo.vo.conversation.ConversationVO;
import com.zzzlew.properties.MinIOConfigProperties;
import com.zzzlew.server.ApplyService;
import com.zzzlew.utils.MinIOFileStorgeUtil;
import com.zzzlew.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/14 - 11 - 14 - 22:35
 * @Description: com.zzzlew.zzzimserver.server.impl
 * @version: 1.0
 */
@Slf4j
@Service
public class ApplyServiceImpl implements ApplyService {

    @Resource
    private ApplyMapper applyMapper;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private ConversationMapper conversationMapper;
    @Resource
    private GroupConversationMapper groupConversationMapper;
    @Resource
    private MinIOFileStorgeUtil minIOFileStorgeUtil;
    @Resource
    private MinIOConfigProperties minIOConfigProperties;

    @Override
    public Long sendApply(SendApplyDTO sendApplyDTO) {
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();
        log.info("当前登录用户id：{}", userId);
        sendApplyDTO.setFromUserId(userId);
        applyMapper.sendApply(sendApplyDTO);
        // 打印好友申请id
        log.info("好友申请表id: {}", sendApplyDTO.getApplyId());
        return sendApplyDTO.getApplyId();
    }

    @Override
    public List<ApplyVO> getApplyList() {
        // 获得当前登录用户的id
        Long userId = UserHolder.getUser().getId();
        // 根据id查询对应的好友列表
        List<ApplyVO> list = applyMapper.getApplyList(userId);
        log.info("好友申请列表: {}", list);
        return list;
    }

    @Transactional
    @Override
    public String dealApply(DealApplyDTO dealApplyDTO) {
        LocalDateTime dealTime = LocalDateTime.now();
        dealApplyDTO.setDealTime(dealTime);
        applyMapper.dealApply(dealApplyDTO);
        // 如果同意好友申请，需要添加好友到好友列表
        if (dealApplyDTO.getDealResult() == 1) {
            // TODO 增加 “幂等性处理”（避免重复添加好友）
            // 获得当前登录用户id
            Long toUserId = UserHolder.getUser().getId();
            Long fromUserId = dealApplyDTO.getFromUserId();

            // TODO 先添加和好友的会话，后续在添加自动发送消息的功能

            String conversationId = toUserId > fromUserId ? String.format("%d_%d", toUserId, fromUserId)
                : String.format("%d_%d", fromUserId, toUserId);

            // 插入会话表
            conversationMapper.insertConversation(conversationId, toUserId, fromUserId.toString(), 0);

            conversationMapper.insertConversation(conversationId, fromUserId, toUserId.toString(), 0);

            // 插入好友关系表
            friendMapper.addFriendToRelation(toUserId, fromUserId);
            friendMapper.addFriendToRelation(fromUserId, toUserId);
            return conversationId;
        }
        return null;
    }

    /**
     * 发送群聊申请
     *
     * @param friendIdList 好友ID列表
     * @param groupApplyDTO 群聊申请信息
     */
    @Transactional
    @Override
    public ConversationVO createGroupConversation(List<Long> friendIdList, GroupApplyDTO groupApplyDTO,
                                                  MultipartFile groupAvatarFile) {
        // 生成群聊的唯一id
        long snowflakeId = IdUtil.getSnowflakeNextId();
        String conversationId = "g_" + snowflakeId;
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();

        // 生成群聊头像的远端存储路径
        String avatarName = conversationId + ".png";
        String minioGroupAvatarPath = conversationId + "/" + avatarName;
        // 上传用户头像到minio服务端
        minIOFileStorgeUtil.uploadAvatar(minioGroupAvatarPath, groupAvatarFile);
        // 生成本地存储远程路径
        String groupAvatar = minIOConfigProperties.getEndpoint() + "/" + minIOConfigProperties.getAvatarBucket() + "/"
            + minioGroupAvatarPath;

        groupApplyDTO.setUserAvatar(groupAvatar);

        groupApplyDTO.setConversationId(conversationId);
        // 插入群聊申请表
        applyMapper.sendGroupApply(userId, friendIdList, groupApplyDTO);

        // 插入群聊会话表
        GroupConversationDTO groupConversationDTO = new GroupConversationDTO();
        groupConversationDTO.setId(conversationId);
        groupConversationDTO.setGroupName(groupApplyDTO.getGroupName());
        groupConversationDTO.setGroupAvatar(groupAvatar);
        groupConversationDTO.setOwnerId(userId);

        groupConversationMapper.insertGroupConversation(groupConversationDTO);

        // 插入群成员表
        GroupMemberDTO groupMemberDTO = new GroupMemberDTO();
        groupMemberDTO.setGroupId(conversationId);
        groupMemberDTO.setUserId(userId);
        groupMemberDTO.setRole(2);
        groupConversationMapper.insertGroupMember(groupMemberDTO);

        // 插入会话表
        conversationMapper.insertConversation(conversationId, userId, conversationId, 1);

        ConversationVO conversationVO = ConversationVO.builder().id(conversationId).avatar(groupAvatar)
            .name(groupApplyDTO.getGroupName()).userId(userId).targetId(conversationId).type(1).build();

        return conversationVO;
    }

    @Override
    public List<GroupApplyVO> getGroupApplyList() {
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();
        // 根据id查询对应的群聊申请列表
        List<GroupApplyVO> list = applyMapper.getGroupApplyList(userId);
        log.info("群聊申请列表: {}", list);
        return list;
    }

    @Transactional
    @Override
    public ConversationVO dealGroupApply(DealGroupDTO dealGroupDTO, MultipartFile groupAvatarBlob) {
        // 获得当前登录用户id
        Long userId = UserHolder.getUser().getId();
        String conversationId = dealGroupDTO.getConversationId();

        // 生成群聊头像的远端存储路径
        String avatarName = conversationId + ".png";
        String minioGroupAvatarPath = conversationId + "/" + avatarName;
        // 上传用户头像到minio服务端
        minIOFileStorgeUtil.uploadAvatar(minioGroupAvatarPath, groupAvatarBlob);
        // 生成本地存储远程路径
        String groupAvatar = minIOConfigProperties.getEndpoint() + "/" + minIOConfigProperties.getAvatarBucket() + "/"
            + minioGroupAvatarPath;
        dealGroupDTO.setUserAvatar(groupAvatar);
        // 修改群聊申请状态
        applyMapper.dealGroupApply(dealGroupDTO);
        // 更新群会话的头像
        if (dealGroupDTO.getStatus() == 2) {
            // 同意入群申请，需要插入群成员表
            GroupMemberDTO groupMemberDTO = new GroupMemberDTO();
            groupMemberDTO.setGroupId(conversationId);
            groupMemberDTO.setUserId(dealGroupDTO.getMemberId());
            groupMemberDTO.setRole(0);
            groupConversationMapper.insertGroupMember(groupMemberDTO);
            // 更新群聊会话表的群成员数量和头像
            groupConversationMapper.updateGroupConversation(conversationId, groupAvatar);
            // 插入会话列表
            conversationMapper.insertConversation(conversationId, userId, conversationId, 1);
            // 查询群会话列表
            ConversationVO conversationVO = groupConversationMapper.selectGroupConversation(conversationId);
            conversationVO.setId(conversationId);
            conversationVO.setTargetId(conversationId);
            conversationVO.setUserId(userId);
            conversationVO.setType(1);
            return conversationVO;
        } else {
            log.info("用户id：{}拒绝入群申请", userId);
            return null;
        }
    }

}
