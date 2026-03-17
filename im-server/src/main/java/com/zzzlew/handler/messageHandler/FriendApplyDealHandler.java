package com.zzzlew.handler.messageHandler;

import cn.hutool.core.bean.BeanUtil;
import com.zzzlew.domain.request.FriendApplyDealRequestDTO;
import com.zzzlew.domain.response.FriendApplyDealResponseVO;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.result.MessageResult;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/17 - 03 - 17 - 17:47
 * @Description: com.zzzlew.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class FriendApplyDealHandler implements MessageHandler<FriendApplyDealRequestDTO> {
    @Override
    public MessageResult handle(ChannelHandlerContext ctx, FriendApplyDealRequestDTO friendApplyDealRequestDTO) {
        log.info("收到好友申请处理消息: {}", friendApplyDealRequestDTO);
        FriendApplyDealResponseVO friendApplyDealResponseVO = BeanUtil.copyProperties(friendApplyDealRequestDTO, FriendApplyDealResponseVO.class);
        Integer dealResult = friendApplyDealRequestDTO.getDealResult();
        // 发送方的id
        Long receiverId = friendApplyDealRequestDTO.getFriendId();
        Long userId = friendApplyDealRequestDTO.getUserId();
        String conversationId = userId > receiverId ? String.format("%d_%d", userId, receiverId)
                : String.format("%d_%d", receiverId, userId);
        friendApplyDealResponseVO.setConversationId(conversationId);
        if (dealResult == 0) {
            // 同意
            friendApplyDealResponseVO.setRelationStatus(1);
        } else {
            // 拒绝
            friendApplyDealResponseVO.setRelationStatus(0);
        }
        // 向好友发送申请处理结果的信息，所以好友接收的信息应该为该用户的信息所以需要更换userId和friendId的顺序
        friendApplyDealResponseVO.setUserId(receiverId);
        friendApplyDealResponseVO.setFriendId(userId);
        log.info("已向接收者{}的channel写入好友申请处理消息:{}", receiverId, friendApplyDealResponseVO);
        return MessageResult.single(friendApplyDealResponseVO, receiverId);
    }

}
