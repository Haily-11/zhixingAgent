package com.haily.zhixing_travel_assistant_backend.config;

import com.haily.zhixing_travel_assistant_backend.service.ChatService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 为ChatService接口创建动态代理实例
 */
@Configuration
public class AgentFactory {

    /**
     * 普通对话模型，调用后一次性返回完整结果
     */
    @Autowired
    private ChatModel chatModel;

    /**
     * 流式对话模型，调用后逐步返回 token
     */
    @Autowired
    private StreamingChatModel streamingChatModel;

    @Bean
    public ChatService nomalService() {
        return AiServices.builder(ChatService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.builder()
                                .id(memoryId)
                                .maxMessages(1000)
                                .build())
                .build();
    }
}
