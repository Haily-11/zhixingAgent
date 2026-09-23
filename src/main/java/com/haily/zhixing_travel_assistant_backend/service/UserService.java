package com.haily.zhixing_travel_assistant_backend.service;


import com.haily.zhixing_travel_assistant_backend.model.dto.LoginRequest;
import com.haily.zhixing_travel_assistant_backend.model.dto.RegisterRequest;
import com.haily.zhixing_travel_assistant_backend.model.dto.UserInfo;

public interface UserService {
    void register(RegisterRequest req);

    UserInfo login(LoginRequest req);

    void logout(String userId);
}