package com.zzzlew.domain.response;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/20 - 11 - 20 - 14:19
 * @Description: com.zzzlew.zzzimserver.pojo.vo.apply
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyResponseVO extends Message implements Serializable {

    /**
     * 申请表id
     */
    private Long applyId;

    private Long fromUserId;

    private String avatar;

    private String username;

    private String applyMsg;

    /**
     * 0：未处理，1：已处理
     */
    private Integer isDealt;

    /**
     * 0：拒绝，1：同意
     */
    private Integer dealResult;

    @Override
    public int getMessageType() {
        return ApplyResponseVO;
    }
}
