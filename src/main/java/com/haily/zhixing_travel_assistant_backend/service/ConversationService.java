package com.haily.zhixing_travel_assistant_backend.service;

import com.haily.zhixing_travel_assistant_backend.model.dto.ConversationListResp;

import java.util.List;

public interface ConversationService {

    /**
     * 查询
     * @param userId
     * @return
     */
    List<ConversationListResp> listByUid(String userId);

    /**
     * 创建会话
     * @param userId
     * @return
     */
    ConversationListResp create(String userId);
}
