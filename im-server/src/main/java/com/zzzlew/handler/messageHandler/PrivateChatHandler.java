package com.zzzlew.handler.messageHandler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.zzzlew.domain.request.PrivateChatRequestDTO;
import com.zzzlew.domain.response.PrivateChatResponseVO;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 21:25
 * @Description: com.zzzlew.zzzimserver.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class PrivateChatHandler extends SimpleChannelInboundHandler<PrivateChatRequestDTO> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PrivateChatRequestDTO privateChatRequestDTO)
            throws Exception {
        // 处理私聊消息
        log.info("收到私聊消息：{}", privateChatRequestDTO);
        // 获得当前登录用户id
        Long userId = ChannelManageUtil.getUser(ctx.channel()).getId();
        // 后端重新生成雪花id
        privateChatRequestDTO.setId(IdUtil.getSnowflakeNextId());
        privateChatRequestDTO.setSenderId(userId);
        // 重置发送时间，让数据库可以自动填充
        privateChatRequestDTO.setSendTime(null);
        // 获得接收者id
        Long receiverId = privateChatRequestDTO.getReceiverId();
        log.info("私信消息:{}", privateChatRequestDTO);
        // 获取接收者的channel
        Channel channel = ChannelManageUtil.getChannel(receiverId);
        if (channel != null) {
            // 发送消息
            PrivateChatResponseVO privateChatResponseVO =
                    BeanUtil.copyProperties(privateChatRequestDTO, PrivateChatResponseVO.class);
            privateChatResponseVO.setSendTime(LocalDateTime.now());
            channel.writeAndFlush(privateChatResponseVO);
            log.info("已向接收者{}的channel写入私聊消息:{}", receiverId, privateChatResponseVO);
        } else {
            // 接收者不在线
            log.info("接收者{}不在线", receiverId);
        }
    }
}
