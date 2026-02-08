package com.zzzlew.handler.messageHandler;

import com.alibaba.fastjson.JSON;
import com.zzzlew.domain.request.GroupApplyRequestDTO;
import com.zzzlew.domain.response.GroupApplyResponseVO;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class GroupApplySendHandler extends SimpleChannelInboundHandler<GroupApplyRequestDTO> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupApplyRequestDTO groupApplyRequestDTO) throws Exception {
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
        for (Long id : ids) {
            // 获得接收者的channel
            Channel channel = ChannelManageUtil.getChannel(id);
            if (channel != null) {
                // 发送消息
                channel.writeAndFlush(groupApplyResponseVO);
                log.info("已向接收者{}的channel写入群聊申请消息", id);
            } else {
                // 接收者不在线
                log.info("接收者{}不在线", id);
            }
        }
    }

}
