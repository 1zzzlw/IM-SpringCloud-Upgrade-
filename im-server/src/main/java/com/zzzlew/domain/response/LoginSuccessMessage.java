package com.zzzlew.domain.response;


import com.zzzlew.domain.Message;

/**
 * @Auther: zzzlew
 * @Date: 2025/12/3 - 12 - 03 - 22:15
 * @Description: com.zzzlew.zzzimserver.pojo.vo.user
 * @version: 1.0
 */
public class LoginSuccessMessage extends Message {

    // TODO 需要传递用户的基本信息

    @Override
    public int getMessageType() {
        return LoginSuccessMessage;
    }
}
