package com.zzzlew.handler.messageHandler;


import com.zzzlew.domain.request.ApplyRequestDTO;
import com.zzzlew.domain.response.ApplyResponseVO;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.result.MessageResult;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/20 - 11 - 20 - 14:22
 * @Description: com.zzzlew.zzzimserver.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ApplySendHandler implements MessageHandler<ApplyRequestDTO> {

    @Override
    public MessageResult handle(ChannelHandlerContext ctx, ApplyRequestDTO applyRequestDTO) {
        log.info("收到好友申请消息：{}", applyRequestDTO);
        // 获得登录用户id
        Long fromUserId = ChannelManageUtil.getUser(ctx.channel()).getId();
        // 获得目标用户id
        Long toUserId = applyRequestDTO.getToUserId();
        // 构建好友申请响应消息
        ApplyResponseVO applyResponseVO = new ApplyResponseVO();
        applyResponseVO.setApplyId(applyRequestDTO.getApplyId());
        applyResponseVO.setFromUserId(fromUserId);
        applyResponseVO.setAvatar(ChannelManageUtil.getUser(ctx.channel()).getAvatar());
        applyResponseVO.setUsername(ChannelManageUtil.getUser(ctx.channel()).getUsername());
        applyResponseVO.setApplyMsg(applyRequestDTO.getApplyMsg());
        applyResponseVO.setIsDealt(0);
        return MessageResult.single(applyResponseVO, toUserId);
    }

}
