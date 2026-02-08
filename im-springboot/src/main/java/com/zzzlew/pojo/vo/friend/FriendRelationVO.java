package com.zzzlew.pojo.vo.friend;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/12 - 11 - 12 - 23:01
 * @Description: com.zzzlew.zzzimserver.pojo.vo.user
 * @version: 1.0
 */
@Data
public class FriendRelationVO implements Serializable {

    private Long userId;

    private Long friendId;

    private String username;

    private String avatar;

    private String remark;

    // 关系状态 0：未同意 1：正常好友 2：黑名单
    // TODO 可以考虑用枚举表示 后期在这里添加免打扰状态
    private Integer relationStatus;
}
