package com.haily.zhixing_travel_assistant_backend.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haily.zhixing_travel_assistant_backend.model.dto.ApiResult;
import com.haily.zhixing_travel_assistant_backend.model.dto.ConversationListResp;
import com.haily.zhixing_travel_assistant_backend.model.entity.Message;
import com.haily.zhixing_travel_assistant_backend.service.ConversationService;
import com.haily.zhixing_travel_assistant_backend.service.MessageService;
import com.haily.zhixing_travel_assistant_backend.utils.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/list")
    public ApiResult<List<ConversationListResp>> list() {
        String userId = RequestContext.getUserId();
        List<ConversationListResp> res = conversationService.listByUid(userId);
        return ApiResult.success(res);
    }

    @PostMapping("/create")
    public ApiResult<ConversationListResp> create() {
        String userId = RequestContext.getUserId();
        ConversationListResp res = conversationService.create(userId);
        return ApiResult.success(res);
    }

    @GetMapping("/messages")
    public ApiResult<Page<Message>> messages(@RequestParam("conversationId") String conversationId,
                                             @RequestParam(value = "page", defaultValue = "1") long page,
                                             @RequestParam(value = "size", defaultValue = "50") long size) {
        String userId = RequestContext.getUserId();
        return ApiResult.success(messageService.pageByConversation(userId, conversationId, page, size));
    }
}