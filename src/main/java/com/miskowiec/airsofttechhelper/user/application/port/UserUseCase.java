package com.miskowiec.airsofttechhelper.user.application.port;

import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserUseCase {
    RegisterResponse register(String username, String password);
    UpdatePasswordResponse changePassword(UserDetails user, String newPassword);

    @Value
    class RegisterResponse{
        public static RegisterResponse SUCCESS = new RegisterResponse(true, null);
        boolean success;
        String errorMessage;
    }

    @Value
    class UpdatePasswordResponse{
        public static UpdatePasswordResponse SUCCESS = new UpdatePasswordResponse(true, null);
        boolean success;
        String errorMessage;
    }
}
