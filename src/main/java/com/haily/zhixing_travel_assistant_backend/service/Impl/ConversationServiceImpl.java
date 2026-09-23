package com.haily.zhixing_travel_assistant_backend.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haily.zhixing_travel_assistant_backend.mapper.ConversationMapper;
import com.haily.zhixing_travel_assistant_backend.model.dto.ConversationListResp;
import com.haily.zhixing_travel_assistant_backend.model.entity.Conversation;
import com.haily.zhixing_travel_assistant_backend.service.ConversationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

    //根据用户id获取会话列表
    @Override
    public List<ConversationListResp> listByUid(String userId) {
        List<Conversation> res = this.lambdaQuery()
                .eq(Conversation::getUserId, userId)
                .eq(Conversation::getIsDelete, 0)
                .list();

        if (res == null) {
            return new ArrayList<>();
        }

        //转换成响应对象
        return res.stream().map(c -> {
            ConversationListResp resp = new ConversationListResp();
            resp.setId(c.getId());
            resp.setTitle(c.getTitle());
            return resp;
        }).toList();
    }

    @Override
    public ConversationListResp create(String userId) {
        //创建实体对象并设置属性
        Conversation conversation = Conversation.builder().build();
        conversation.setId(UUID.randomUUID().toString());
        conversation.setUserId(userId);
        //保存实体对象
        this.save(conversation);
        return new ConversationListResp(conversation.getId(), conversation.getTitle());
    }
}
