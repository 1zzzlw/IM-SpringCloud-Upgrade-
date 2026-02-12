package com.zzzlew.controller;

import com.zzzlew.pojo.vo.friend.FriendRelationVO;
import com.zzzlew.pojo.vo.user.UserSearchVO;
import com.zzzlew.result.Result;
import com.zzzlew.server.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/12 - 11 - 12 - 22:51
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/friend")
@Tag(name = "好友接口")
public class FriendController {

    @Resource
    private FriendService friendService;

    /**
     * 全量更新并初始化好友列表
     * 
     * @param isInit 是否初始化
     * @return 好友列表
     */
    @Operation(summary = "全量更新并初始化好友列表")
    @GetMapping("/init/list")
    public Result<List<FriendRelationVO>> initFriendList(@RequestParam Boolean isInit) {
        log.info("初始化好友列表: {}", isInit);
        List<FriendRelationVO> friendRelationVOList = friendService.initFriendList(isInit);
        return Result.success(friendRelationVOList);
    }

    /**
     * 搜索用户
     * 
     * @param phone 手机号
     * @return 用户搜索vo
     */
    @Operation(summary = "搜索用户")
    @GetMapping("/search")
    public Result<UserSearchVO> search(String phone) {
        log.info("搜索用户 {} 的信息", phone);
        UserSearchVO userSearchVO = friendService.search(phone);
        return Result.success(userSearchVO);
    }



}
