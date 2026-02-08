package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/20 - 11 - 20 - 14:15
 * @Description: com.zzzlew.zzzimserver.pojo.vo.apply
 * @version: 1.0
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ApplyRequestDTO extends Message implements Serializable {

    private Long applyId;

    private Long fromUserId;

    private Long toUserId;

    private String applyMsg;

    @Override
    public int getMessageType() {
        return ApplyRequestDTO;
    }

}
