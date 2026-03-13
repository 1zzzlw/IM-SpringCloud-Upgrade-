package com.zzzlew.domain.response;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/13 - 03 - 13 - 16:22
 * @Description: com.zzzlew.domain.response
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuitLoginResponseVO extends Message implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    @Override
    public int getMessageType() {
        return QuitLoginResponseVO;
    }
}
