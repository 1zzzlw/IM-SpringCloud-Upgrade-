package com.zzzlew.controller;

import com.zzzlew.pojo.vo.conversation.ConversationVO;
import com.zzzlew.pojo.vo.user.GroupMemberVO;
import com.zzzlew.result.Result;
import com.zzzlew.server.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/21 - 11 - 21 - 21:00
 * @Description: com.zzzlew.zzzimserver.controller
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/conversation")
@Tag(name = "会话接口")
public class ConversationController {

    @Resource
    private ConversationService conversationService;

    /**
     * 全量更新并初始化会话列表
     * 
     * @param isInit 是否初始化
     * @return 会话列表
     */
    @Operation(summary = "全量更新并初始化会话列表")
    @GetMapping("/init/list")
    public Result<List<ConversationVO>> initConversationList(@RequestParam Boolean isInit) {
        log.info("初始化会话列表：{}", isInit);
        List<ConversationVO> conversationVOList = conversationService.initConversationList(isInit);
        log.info("会话列表：{}", conversationVOList);
        return Result.success(conversationVOList);
    }

    /**
     * 获取群聊成员列表
     *
     * @param conversationId 群聊会话ID
     * @return 群聊成员列表
     */
    @Operation(summary = "获取群聊成员列表")
    @GetMapping("/groupMemberList/{conversationId}")
    public Result<List<GroupMemberVO>> getGroupMemberList(@PathVariable String conversationId) {
        List<GroupMemberVO> groupMemberVOList = conversationService.getGroupMemberList(conversationId);
        return Result.success(groupMemberVOList);
    }

    /**
     * 清除会话中未读消息计数
     *
     * @param conversationId 群聊会话ID
     */
    @Operation(summary = "清除会话中未读消息计数")
    @PutMapping("/isReaded/{conversationId}")
    public Result<Object> clearConversationUnreadCounts(@PathVariable String conversationId) {
        conversationService.clearConversationUnreadCounts(conversationId);
        return Result.success();
    }

}