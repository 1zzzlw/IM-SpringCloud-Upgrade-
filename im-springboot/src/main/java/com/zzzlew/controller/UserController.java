package com.zzzlew.controller;


import com.zzzlew.pojo.dto.user.UserRegisterDTO;
import com.zzzlew.pojo.entity.UserAuth;
import com.zzzlew.result.Result;
import com.zzzlew.server.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:07
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户模块")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息
     * @return 注册成功后的token
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserAuth> register(UserRegisterDTO userRegisterDTO,
                                     @RequestParam(value = "avatarFile") MultipartFile avatarFile, HttpServletResponse response) {
        log.info("注册用户信息为 {}，头像信息为 {}", userRegisterDTO, avatarFile);
        UserAuth userAuth = userService.register(userRegisterDTO, avatarFile, response);
        return Result.success(userAuth);
    }

    /**
     * 创建手机号验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    @Operation(summary = "创建手机号验证码")
    @PostMapping("/phoneCode")
    public Result<String> createCode(@RequestParam("phone") String phone) {
        log.info("创建手机号 {} 的验证码", phone);
        String phoneCode = userService.createPhoneCode(phone);
        return Result.success(phoneCode);
    }

}
