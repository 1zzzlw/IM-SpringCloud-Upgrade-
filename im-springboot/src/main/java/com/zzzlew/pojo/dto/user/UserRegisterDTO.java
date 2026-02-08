package com.zzzlew.pojo.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户注册DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends UserBaseDTO implements Serializable {

    private String phoneCode;

    private Integer gender;

    private String phone;

    private LocalDate birthday;

}
