package com.haily.zhixing_travel_assistant_backend.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface ChatService {

    @SystemMessage("你是一个智能聊天机器人，请根据用户输入回答问题，尽量简洁一点")
    Flux<String> chat(@MemoryId String sessionId, @UserMessage String prompt);
    
}
