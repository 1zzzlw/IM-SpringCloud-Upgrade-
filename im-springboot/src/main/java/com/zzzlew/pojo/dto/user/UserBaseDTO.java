package com.zzzlew.pojo.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户基础信息DTO
 */
@Data
public class UserBaseDTO implements Serializable {
    private Long id;

    private String username;

    private String account;

    private String password;

    private String avatar;

}
