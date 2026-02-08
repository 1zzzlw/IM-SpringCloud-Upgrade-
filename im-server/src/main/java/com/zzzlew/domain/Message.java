package com.zzzlew.domain;


import com.zzzlew.domain.request.*;
import com.zzzlew.domain.response.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 15:46
 * @Description: com.zzzlew.zzzimserver.pojo
 * @version: 1.0
 */
@Data
public abstract class Message implements Serializable {
    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int PrivateChatRequestDTO = 1;
    public static final int PrivateChatResponseVO = 2;

    public static final int GroupChatRequestDTO = 3;
    public static final int GroupChatResponseVO = 4;

    public static final int ApplyRequestDTO = 5;
    public static final int ApplyResponseVO = 6;

    public static final int GroupApplyRequestDTO = 7;
    public static final int GroupApplyResponseVO = 8;

    public static final int LoginSuccessMessage = 9;

    public static final int FileRequestDTO = 10;
    public static final int FileResponseVO = 11;

    /**
     * 根据消息类型字节，获得对应的消息 class
     *
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    static {
        messageClasses.put(PrivateChatRequestDTO, PrivateChatRequestDTO.class);
        messageClasses.put(PrivateChatResponseVO, PrivateChatResponseVO.class);
        messageClasses.put(GroupChatRequestDTO, GroupChatRequestDTO.class);
        messageClasses.put(GroupChatResponseVO, GroupChatResponseVO.class);
        messageClasses.put(ApplyRequestDTO, ApplyRequestDTO.class);
        messageClasses.put(ApplyResponseVO, ApplyResponseVO.class);
        messageClasses.put(GroupApplyRequestDTO, GroupApplyRequestDTO.class);
        messageClasses.put(GroupApplyResponseVO, GroupApplyResponseVO.class);
        messageClasses.put(LoginSuccessMessage, LoginSuccessMessage.class);
        messageClasses.put(FileRequestDTO, FileRequestDTO.class);
        messageClasses.put(FileResponseVO, FileResponseVO.class);
    }

}
