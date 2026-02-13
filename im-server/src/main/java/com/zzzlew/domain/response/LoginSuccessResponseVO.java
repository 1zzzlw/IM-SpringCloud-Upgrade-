package com.zzzlew.domain.response;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: zzzlew
 * @Date: 2025/12/3 - 12 - 03 - 22:15
 * @Description: com.zzzlew.zzzimserver.pojo.vo.user
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginSuccessResponseVO extends Message {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户名称
     */
    private String name;


    @Override
    public int getMessageType() {
        return LoginSuccessResponseVO;
    }
}
