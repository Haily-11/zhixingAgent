package com.haily.zhixing_travel_assistant_backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haily.zhixing_travel_assistant_backend.agent.requirement.RequirementAgent;
import com.haily.zhixing_travel_assistant_backend.mapper.ConversationMapper;
import com.haily.zhixing_travel_assistant_backend.mapper.StoreMapper;
import com.haily.zhixing_travel_assistant_backend.model.entity.Conversation;
import com.haily.zhixing_travel_assistant_backend.model.entity.Message;
import com.haily.zhixing_travel_assistant_backend.service.ChatService;
import com.haily.zhixing_travel_assistant_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private RequirementAgent requirementAgent;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Flux<String> chat(String userId, String sessionId, String prompt) {
        return Mono.fromCallable(() -> {
            //1.确保会话存在 + 存用户消息
                    ensureConversationExists(userId, sessionId);
                    insertMessage(sessionId, (short) 1, prompt);
                    return true;
                })
                .subscribeOn(Schedulers.boundedElastic())
                //2.执行AI工作流requirementAgent.executeState(sessionId, prompt)
                .then(requirementAgent.executeState(sessionId, prompt))
                .flatMap(state -> Mono.fromCallable(() -> {
                    //3.保存AI回复
                            //3.1 获取AI回复
                            String output = state.output();
                            //3.2 插入AI回复
                            insertMessage(sessionId, (short) 2, output);
                            //3.3 更新会话最后更新时间
                            updateConversationUpdatedAt(sessionId);
                            //3.4 存储会话计划
                            java.util.Map<String, Object> value = new java.util.HashMap<>();
                            value.put("requirements", state.requirements());
                            value.put("output", output);
                            storeMapper.upsert(new String[]{"conversations", sessionId}, "latest_plan", objectMapper.writeValueAsString(value));
                            return output;
                        }).subscribeOn(Schedulers.boundedElastic())
                )
                .onErrorResume(e -> Mono.just("系统繁忙，请稍后再试"))
                .flatMapMany(Flux::just);
    }

    /**
     *
     * 确保会话存在：先查，存在就什么都不做；不存在才创建并插入。
     * @param userId
     * @param conversationId
     */
    private void ensureConversationExists(String userId, String conversationId) {
        Conversation existing = conversationMapper.selectOne(new LambdaQueryWrapper<Conversation>()
                .eq(Conversation::getId, conversationId)
                .eq(Conversation::getUserId, userId)
                .eq(Conversation::getIsDelete, 0)
                .last("limit 1"));

        if (existing != null) {
            return;
        }

        Conversation conversation = Conversation.builder().build();
        conversation.setId(conversationId);
        conversation.setUserId(userId);
        conversation.setTitle("新的会话");
        conversation.setCreatedAt(java.time.OffsetDateTime.now());
        conversation.setUpdatedAt(java.time.OffsetDateTime.now());
        conversation.setIsDelete((short) 0);
        conversationMapper.insert(conversation);
    }

    private void updateConversationUpdatedAt(String conversationId) {
        Conversation conversation = Conversation.builder().build();
        conversation.setId(conversationId);
        conversation.setUpdatedAt(java.time.OffsetDateTime.now());
        conversationMapper.updateById(conversation);
    }

    private void insertMessage(String conversationId, short role, String content) {
        Message message = Message.builder().build();
        message.setId(java.util.UUID.randomUUID().toString());
        message.setConversationId(conversationId);
        message.setRole(role);
        message.setContent(content == null ? "" : content);
        message.setIsDelete((short) 0);
        messageService.save(message);
    }
}