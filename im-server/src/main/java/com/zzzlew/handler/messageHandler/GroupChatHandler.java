package com.zzzlew.handler.messageHandler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.zzzlew.domain.request.GroupChatRequestDTO;
import com.zzzlew.domain.response.ACKMessageResponseVO;
import com.zzzlew.domain.response.GroupChatResponseVO;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.result.MessageResult;
import com.zzzlew.utils.ACKMessagePackUtil;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
public class GroupChatHandler implements MessageHandler<GroupChatRequestDTO> {

    @Override
    public MessageResult handle(ChannelHandlerContext ctx, GroupChatRequestDTO groupChatRequestDTO) {
        // 处理群聊消息
        log.info("收到群聊消息: {}", groupChatRequestDTO);
        // 获得当前登录用户id
        Long userId = ChannelManageUtil.getUser(ctx.channel()).getId();
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
        // 生成服务端的唯一id
        groupChatResponseVO.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
        log.info("群聊回应的消息为: {}", groupChatResponseVO);
        // 返回发送消息成功发送服务端的ACK消息
        ACKMessageResponseVO successACK = ACKMessagePackUtil.createSuccessACK(String.valueOf(groupChatRequestDTO.getId()), groupChatResponseVO.getId());
        log.info("ACK消息为：{}", successACK);
        ctx.channel().writeAndFlush(successACK);
        return MessageResult.multiple(groupChatResponseVO, receiverIds);
    }

}
