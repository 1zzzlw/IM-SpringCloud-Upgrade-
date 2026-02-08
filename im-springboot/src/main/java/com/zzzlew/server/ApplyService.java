package com.zzzlew.server;


import com.zzzlew.pojo.dto.apply.DealApplyDTO;
import com.zzzlew.pojo.dto.apply.DealGroupDTO;
import com.zzzlew.pojo.dto.apply.GroupApplyDTO;
import com.zzzlew.pojo.dto.apply.SendApplyDTO;
import com.zzzlew.pojo.vo.apply.ApplyVO;
import com.zzzlew.pojo.vo.apply.GroupApplyVO;
import com.zzzlew.pojo.vo.conversation.ConversationVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/14 - 11 - 14 - 22:34
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface ApplyService {

    /**
     * 发送好友申请
     *
     * @param sendApplyDTO 好友申请信息
     */
    Long sendApply(SendApplyDTO sendApplyDTO);

    /**
     * 获取好友申请列表
     *
     * @return 好友申请列表
     */
    List<ApplyVO> getApplyList();

    /**
     * 同意好友申请
     *
     * @param dealApplyDTO 好友申请信息
     */
    String dealApply(DealApplyDTO dealApplyDTO);

    /**
     * 发送群聊申请
     *
     * @param friendIdList  好友ID列表
     * @param groupApplyDTO 群聊申请信息
     */
    ConversationVO createGroupConversation(List<Long> friendIdList, GroupApplyDTO groupApplyDTO, MultipartFile groupAvatar);

    /**
     * 获取群聊申请列表
     *
     * @return 群聊申请列表
     */
    List<GroupApplyVO> getGroupApplyList();

    /**
     * 同意入群申请
     *
     * @param dealGroupDTO 群聊申请处理信息
     */
    ConversationVO dealGroupApply(DealGroupDTO dealGroupDTO, MultipartFile groupAvatarBlob);

}
