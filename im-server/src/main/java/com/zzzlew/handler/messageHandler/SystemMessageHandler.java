package com.zzzlew.handler.messageHandler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.zzzlew.domain.request.SystemMessageRequestDTO;
import com.zzzlew.domain.response.ACKMessageResponseVO;
import com.zzzlew.domain.response.SystemMessageResponseVO;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.result.MessageResult;
import com.zzzlew.utils.ACKMessagePackUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @Auther: zzzlew
 * @Date: 2026/3/10 - 03 - 10 - 21:33
 * @Description: com.zzzlew.handler.messageHandler
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class SystemMessageHandler implements MessageHandler<SystemMessageRequestDTO> {

    @Override
    public MessageResult handle(ChannelHandlerContext ctx, SystemMessageRequestDTO systemMessageRequestDTO) {
        log.info("收到系统消息：{}", systemMessageRequestDTO);
        List<Long> receiverIds = systemMessageRequestDTO.getReceiverIds();
        SystemMessageResponseVO systemMessageResponseVO = BeanUtil.copyProperties(systemMessageRequestDTO, SystemMessageResponseVO.class);
        systemMessageResponseVO.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
        log.info("回应的系统消息：{}", systemMessageResponseVO);
        // 返回发送消息成功发送服务端的ACK消息
        ACKMessageResponseVO successACK = ACKMessagePackUtil.createSuccessACK(String.valueOf(systemMessageRequestDTO.getId()), systemMessageResponseVO.getId());
        log.info("ACK消息为：{}", successACK);
        ctx.channel().writeAndFlush(successACK);
        // 由于有了统一的发布订阅的模式，群聊和单聊其实可以合并了。。。
        return MessageResult.multiple(systemMessageResponseVO, receiverIds);
    }

}
