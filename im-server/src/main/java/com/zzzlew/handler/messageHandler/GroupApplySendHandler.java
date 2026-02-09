package com.zzzlew.handler.messageHandler;

import com.alibaba.fastjson.JSON;
import com.zzzlew.domain.request.GroupApplyRequestDTO;
import com.zzzlew.domain.response.GroupApplyResponseVO;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.result.MessageResult;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/22 - 11 - 22 - 17:30
 * @Description: com.zzzlew.zzzimserver.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupApplySendHandler implements MessageHandler<GroupApplyRequestDTO> {

    @Override
    public MessageResult handle(ChannelHandlerContext ctx, GroupApplyRequestDTO groupApplyRequestDTO) {
        log.info("收到群聊申请请求：{}", groupApplyRequestDTO);
        // 获得当前登录用户id
        Long userId = ChannelManageUtil.getUser(ctx.channel()).getId();
        // 获得申请用户id集合
        String invitedIds = groupApplyRequestDTO.getInvitedIds();
        List<Long> ids = JSON.parseArray(invitedIds, Long.class);
        GroupApplyResponseVO groupApplyResponseVO = new GroupApplyResponseVO();
        groupApplyResponseVO.setConversationId(groupApplyRequestDTO.getConversationId());
        groupApplyResponseVO.setUserId(userId);
        groupApplyResponseVO.setUserAvatar(groupApplyRequestDTO.getUserAvatar());
        groupApplyResponseVO.setGroupName(groupApplyRequestDTO.getGroupName());
        groupApplyResponseVO.setStatus(1);
        return MessageResult.multiple(groupApplyResponseVO, ids);
    }

}
