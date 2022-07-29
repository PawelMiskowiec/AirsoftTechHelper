package com.example.airsofttechhelper.user.application.port;

import com.example.airsofttechhelper.user.domain.UserEntity;
import lombok.Value;

import java.util.Collections;
import java.util.List;

public interface UserRegistrationUseCase {
    RegisterResponse register(String username, String password);

    @Value
    class RegisterResponse{
        public static RegisterResponse SUCCESS = new RegisterResponse(true, null);
        boolean success;
        String errorMessage;
    }
}
