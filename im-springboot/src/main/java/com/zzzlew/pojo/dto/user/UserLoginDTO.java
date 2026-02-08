package com.zzzlew.pojo.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户登录DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginDTO extends UserBaseDTO implements Serializable {

    private String verifyCode;

}
