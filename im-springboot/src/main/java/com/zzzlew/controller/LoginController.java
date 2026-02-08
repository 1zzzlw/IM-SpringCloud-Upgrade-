package com.zzzlew.controller;

import com.zzzlew.pojo.dto.user.UserLoginDTO;
import com.zzzlew.pojo.vo.user.UserInfoVO;
import com.zzzlew.result.Result;
import com.zzzlew.server.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:37
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/login")
@Tag(name = "登录接口")
public class LoginController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * 
     * @param userLoginDTO 用户登录信息
     * @return 用户登录vo
     */
    @Operation(summary = "用户登录")
    @PostMapping
    public Result<UserInfoVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        log.info("当前登录用户信息：{}", userLoginDTO);
        UserInfoVO userInfoVO = userService.login(userLoginDTO, response);
        log.info("登录成功，当前登录用户信息：{}", userInfoVO);
        return Result.success(userInfoVO);
    }

    /**
     * 生成登录验证码
     * 
     * @param response HttpServletResponse
     */
    @Operation(summary = "生成登录验证码")
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletResponse response) {
        log.info("随机生成登录验证码");
        userService.createCode(response);
    }

    /**
     * 用户登录确认
     * 
     * @param token 登录凭证
     * @param userId 用户id
     * @return 登录确认结果
     */
    @Operation(summary = "用户登录确认")
    @GetMapping("/pendingLogin")
    public Result<Object> pendingLogin(@RequestParam("token") String token, @RequestParam("userId") Long userId, HttpServletResponse response) {
        log.info("用户 {} 正在登录，用户id为 {}", token, userId);
        userService.pendingLogin(token, userId, response);
        return Result.success();
    }
}
