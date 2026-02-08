package com.zzzlew.controller;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/17 - 11 - 17 - 20:07
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */


import com.zzzlew.result.Result;
import com.zzzlew.server.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用控制器类
 */
@Slf4j
@RestController
@RequestMapping("/commons")
@Tag(name = "通用接口")
public class CommonsController {

    @Resource
    private UserService userService;

    @Operation(summary = "刷新token")
    @PostMapping("/refreshToken/{userId}")
    public Result<String> refreshToken(@PathVariable("userId") Long userId, HttpServletResponse response) {
        log.info("用户id为：{} 开始刷新AccessToken", userId);
        userService.refreshToken(userId, response);
        return Result.success();
    }

}
