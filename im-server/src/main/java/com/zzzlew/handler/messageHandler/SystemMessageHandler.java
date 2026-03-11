package com.zzzlew.handler.messageHandler;

import cn.hutool.core.bean.BeanUtil;
import com.zzzlew.domain.request.SystemMessageRequestDTO;
import com.zzzlew.domain.response.SystemMessageResponseVO;
import com.zzzlew.enums.SystemMessage;
import com.zzzlew.handler.impl.MessageHandler;
import com.zzzlew.result.MessageResult;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


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
        Integer msgType = systemMessageRequestDTO.getMessageType();
        String content = SystemMessage.getByValue(msgType).getDescription();
        Long receiverId = systemMessageRequestDTO.getReceiverId();
        SystemMessageResponseVO systemMessageResponseVO = BeanUtil.copyProperties(systemMessageRequestDTO, SystemMessageResponseVO.class);
        systemMessageResponseVO.setContent(content);
        systemMessageResponseVO.setReceiverId("system");
        return MessageResult.single(systemMessageResponseVO, receiverId);
    }

}
