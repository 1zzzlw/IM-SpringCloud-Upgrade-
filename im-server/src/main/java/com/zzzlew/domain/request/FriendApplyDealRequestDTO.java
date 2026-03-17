package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Auther: zzzlew
 * @Date: 2026/3/17 - 03 - 17 - 17:50
 * @Description: com.zzzlew.domain.request
 * @version: 1.0
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class FriendApplyDealRequestDTO extends Message implements Serializable {

    /**
     * 用户id
     */
    private Long userId;


    /**
     * 好友id
     */
    private Long friendId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String account;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 0：未知 1：男 2：女
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态 0：未同意 1：正常好友 2：拉黑
     */
    private Integer dealResult;

    @Override
    public int getMessageType() {
        return FriendApplyDealRequestDTO;
    }
}
