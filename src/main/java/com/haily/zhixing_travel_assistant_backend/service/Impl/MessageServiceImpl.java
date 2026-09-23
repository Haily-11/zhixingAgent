package com.haily.zhixing_travel_assistant_backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haily.zhixing_travel_assistant_backend.enums.CommonError;
import com.haily.zhixing_travel_assistant_backend.exception.BusinessException;
import com.haily.zhixing_travel_assistant_backend.mapper.ConversationMapper;
import com.haily.zhixing_travel_assistant_backend.mapper.MessageMapper;
import com.haily.zhixing_travel_assistant_backend.model.entity.Conversation;
import com.haily.zhixing_travel_assistant_backend.model.entity.Message;
import com.haily.zhixing_travel_assistant_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private ConversationMapper conversationMapper;

    @Override
    public Page<Message> pageByConversation(String userId, String conversationId, long page, long size) {
        Conversation conversation = conversationMapper.selectOne(new LambdaQueryWrapper<Conversation>()
                .eq(Conversation::getId, conversationId)
                .eq(Conversation::getUserId, userId)
                .eq(Conversation::getIsDelete, 0)
                .last("limit 1"));
        if (conversation == null) {
            throw new BusinessException(CommonError.INVALID_PARAMETER_ERROR.getCode(), "会话不存在");
        }

        long safePage = Math.max(1, page);
        long safeSize = Math.min(200, Math.max(1, size));

        Page<Message> p = new Page<>(safePage, safeSize);
        return this.lambdaQuery()
                .eq(Message::getConversationId, conversationId)
                .eq(Message::getIsDelete, 0)
                .orderByAsc(Message::getCreatedAt)
                .page(p);
    }
}