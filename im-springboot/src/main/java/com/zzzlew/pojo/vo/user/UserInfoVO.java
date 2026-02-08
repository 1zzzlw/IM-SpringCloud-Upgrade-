package com.zzzlew.pojo.vo.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:29
 * @Description: com.zzzlew.zzzimserver.pojo.vo
 * @version: 1.0
 */
@Builder
@Data
public class UserInfoVO implements Serializable {
    private Long id;

    private String username;

    private String account;

    private String avatar;

    // 是否在线 0-不在线 1-在线
    private Integer onLine;
}
