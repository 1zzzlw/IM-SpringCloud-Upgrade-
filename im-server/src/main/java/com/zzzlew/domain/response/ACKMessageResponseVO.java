package com.zzzlew.domain.response;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/24 - 03 - 24 - 21:10
 * @Description: com.zzzlew.domain.response
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ACKMessageResponseVO extends Message implements Serializable {

    private String tempId;

    private String messageId;

    private Integer status;

    private String errorCase;

    @Override
    public int getMessageType() {
        return ACKMessageResponseVO;
    }

}
