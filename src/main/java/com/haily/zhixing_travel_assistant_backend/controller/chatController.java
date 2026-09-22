package com.haily.zhixing_travel_assistant_backend.controller;


import com.azure.core.annotation.Post;
import com.haily.zhixing_travel_assistant_backend.model.dto.ApiResult;
import com.haily.zhixing_travel_assistant_backend.model.dto.ChatRequest;
import com.haily.zhixing_travel_assistant_backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/travel")
public class chatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/stream/chat")
    public Flux<String> streamChat(@RequestBody ChatRequest chatRequest){
        return chatService.chat(chatRequest.getSessionId(),chatRequest.getPrompt());
    }
}
