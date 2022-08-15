package com.miskowiec.airsofttechhelper.user.application.port;

import lombok.Value;

public interface UserRegistrationUseCase {
    RegisterResponse register(String username, String password);

    @Value
    class RegisterResponse{
        public static RegisterResponse SUCCESS = new RegisterResponse(true, null);
        boolean success;
        String errorMessage;
    }
}
