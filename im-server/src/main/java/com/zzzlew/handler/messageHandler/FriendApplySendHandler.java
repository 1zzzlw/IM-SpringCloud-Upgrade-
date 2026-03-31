package com.zzzlew.handler.messageHandler;


import com.zzzlew.domain.request.FriendApplyRequestDTO;
import com.zzzlew.domain.response.FriendApplyResponseVO;
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
public class FriendApplySendHandler implements MessageHandler<FriendApplyRequestDTO> {

    @Override
    public MessageResult handle(ChannelHandlerContext ctx, FriendApplyRequestDTO friendApplyRequestDTO) {
        log.info("收到好友申请消息：{}", friendApplyRequestDTO);
        // 获得登录用户id
        Long fromUserId = ChannelManageUtil.getUser(ctx.channel()).getId();
        // 获得目标用户id
        Long toUserId = friendApplyRequestDTO.getToUserId();
        // 构建好友申请响应消息
        FriendApplyResponseVO friendApplyResponseVO = new FriendApplyResponseVO();
        friendApplyResponseVO.setApplyId(friendApplyRequestDTO.getApplyId());
        friendApplyResponseVO.setFromUserId(fromUserId);
        friendApplyResponseVO.setAvatar(ChannelManageUtil.getUser(ctx.channel()).getAvatar());
        friendApplyResponseVO.setUsername(ChannelManageUtil.getUser(ctx.channel()).getUsername());
        friendApplyResponseVO.setAccount(ChannelManageUtil.getUser(ctx.channel()).getAccount());
        friendApplyResponseVO.setGender(ChannelManageUtil.getUser(ctx.channel()).getGender());
        friendApplyResponseVO.setPhone(ChannelManageUtil.getUser(ctx.channel()).getPhone());
        friendApplyResponseVO.setApplyMsg(friendApplyRequestDTO.getApplyMsg());
        friendApplyResponseVO.setIsDealt(0);
        log.info("发送好友申请消息：{}", friendApplyResponseVO);
        return MessageResult.single(friendApplyResponseVO, toUserId);
    }

}
