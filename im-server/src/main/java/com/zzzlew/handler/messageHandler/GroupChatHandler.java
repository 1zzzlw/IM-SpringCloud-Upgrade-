package com.zzzlew.handler.messageHandler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.zzzlew.domain.request.GroupChatRequestDTO;
import com.zzzlew.domain.response.GroupChatResponseVO;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 21:25
 * @Description: com.zzzlew.zzzimserver.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupChatHandler extends SimpleChannelInboundHandler<GroupChatRequestDTO> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestDTO groupChatRequestDTO) throws Exception {
        // 处理群聊消息
        log.info("收到群聊消息: {}", groupChatRequestDTO);
        // 获得当前登录用户id
        Long userId = ChannelManageUtil.getUser(ctx.channel()).getId();
        // 后端重新生成雪花id
        groupChatRequestDTO.setId(IdUtil.getSnowflakeNextId());
        groupChatRequestDTO.setSenderId(userId);
        // 重置发送时间，数据库可以自动填充
        groupChatRequestDTO.setSendTime(null);
        // 获得接收者id列表
        List<Long> receiverIds = groupChatRequestDTO.getReceiverIds();
        String receiverId = groupChatRequestDTO.getReceiverId();
        LocalDateTime sendTime = LocalDateTime.now();
        GroupChatResponseVO groupChatResponseVO =
            BeanUtil.copyProperties(groupChatRequestDTO, GroupChatResponseVO.class);
        groupChatResponseVO.setReceiverId(receiverId);
        groupChatResponseVO.setSendTime(sendTime);
        log.info("群聊回应的消息为: {}", groupChatResponseVO);
        // 遍历接收者id列表
        for (Long receiver : receiverIds) {
            if (receiver.equals(userId)) {
                continue;
            }
            // 拿到接收者的channel
            Channel channel = ChannelManageUtil.getChannel(receiver);
            if (channel != null) {
                // 发送消息
                channel.writeAndFlush(groupChatResponseVO);
                log.info("向用户{}发送群聊消息: {}", receiver, groupChatResponseVO);
            } else {
                log.warn("用户{}的channel不存在", receiver);
            }
        }
    }

}
