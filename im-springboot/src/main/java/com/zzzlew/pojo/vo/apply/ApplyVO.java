package com.zzzlew.pojo.vo.apply;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/15 - 11 - 15 - 0:08
 * @Description: com.zzzlew.zzzimserver.pojo.vo.user
 * @version: 1.0
 */
import lombok.Data;

import java.io.Serializable;

@Data
public class ApplyVO implements Serializable {

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

}
