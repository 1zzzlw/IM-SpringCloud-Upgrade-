package com.zzzlew.pojo.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户详细信息DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoDTO extends UserBaseDTO implements Serializable {
    private Integer gender;

    private String phone;

    private String email;

    private LocalDate birthday;

    private String address;
}
