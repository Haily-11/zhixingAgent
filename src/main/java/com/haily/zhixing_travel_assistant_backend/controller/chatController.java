package com.haily.zhixing_travel_assistant_backend.controller;


import com.azure.core.annotation.Post;
import com.haily.zhixing_travel_assistant_backend.model.dto.ApiResult;
import com.haily.zhixing_travel_assistant_backend.model.dto.ChatRequest;
import com.haily.zhixing_travel_assistant_backend.service.ChatService;
import com.haily.zhixing_travel_assistant_backend.utils.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/travel")
public class chatController {

    @Autowired
    private ChatService chatService;

    @PostMapping(value = "/stream/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> streamChat(@RequestBody ChatRequest chatRequest) {
        //从线程上下文获取用户id
        String userId = RequestContext.getUserId();
        return chatService.chat(userId, chatRequest.getSessionId(), chatRequest.getPrompt());
    }
}
