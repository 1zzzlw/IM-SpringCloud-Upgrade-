package com.zzzlew.handler.messageHandler;


import com.zzzlew.domain.request.ApplyRequestDTO;
import com.zzzlew.domain.response.ApplyResponseVO;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/20 - 11 - 20 - 14:22
 * @Description: com.zzzlew.zzzimserver.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ApplySendHandler extends SimpleChannelInboundHandler<ApplyRequestDTO> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ApplyRequestDTO applyRequestDTO) throws Exception {
        log.info("收到好友申请消息：{}", applyRequestDTO);
        // 获得登录用户id
        Long fromUserId = ChannelManageUtil.getUser(ctx.channel()).getId();
        // 获得目标用户id
        Long toUserId = applyRequestDTO.getToUserId();
        // 获取目标用户的channel
        Channel toUserChannel = ChannelManageUtil.getChannel(toUserId);
        // 如果目标用户不在线，直接返回
        if (toUserChannel != null) {
            // 构建好友申请响应消息
            ApplyResponseVO applyResponseVO = new ApplyResponseVO();
            applyResponseVO.setApplyId(applyRequestDTO.getApplyId());
            applyResponseVO.setFromUserId(fromUserId);
            applyResponseVO.setAvatar(ChannelManageUtil.getUser(ctx.channel()).getAvatar());
            applyResponseVO.setUsername(ChannelManageUtil.getUser(ctx.channel()).getUsername());
            applyResponseVO.setApplyMsg(applyRequestDTO.getApplyMsg());
            applyResponseVO.setIsDealt(0);

            // 向目标用户的channel写入好友申请消息
            toUserChannel.writeAndFlush(applyResponseVO);
            log.info("已向目标用户{}的channel写入好友申请消息", toUserId);
        } else {
            log.info("目标用户不在线");
        }
    }

}
