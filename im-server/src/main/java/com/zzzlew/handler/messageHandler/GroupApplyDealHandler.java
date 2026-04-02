package com.zzzlew.handler.messageHandler;

import cn.hutool.core.bean.BeanUtil;
import com.zzzlew.domain.request.GroupApplyDealRequestDTO;
import com.zzzlew.domain.response.GroupApplyDealResponseVO;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.result.MessageResult;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/17 - 03 - 17 - 17:47
 * @Description: com.zzzlew.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupApplyDealHandler implements MessageHandler<GroupApplyDealRequestDTO> {
    @Override
    public MessageResult handle(ChannelHandlerContext ctx, GroupApplyDealRequestDTO groupApplyDealRequestDTO) {
        log.info("收到群申请处理消息：{}", groupApplyDealRequestDTO);
        GroupApplyDealResponseVO groupApplyDealResponseVO = BeanUtil.copyProperties(groupApplyDealRequestDTO, GroupApplyDealResponseVO.class);
        List<Long> receiverIds = groupApplyDealRequestDTO.getReceiverIds();
        return MessageResult.multiple(groupApplyDealResponseVO, receiverIds);
    }

}
