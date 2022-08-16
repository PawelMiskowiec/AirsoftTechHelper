package com.miskowiec.airsofttechhelper.user.web;

import com.miskowiec.airsofttechhelper.security.UserSecurity;
import com.miskowiec.airsofttechhelper.user.application.port.UserUseCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import java.util.Map;

import static com.miskowiec.airsofttechhelper.user.application.port.UserUseCase.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
    private final UserUseCase registrationService;
    private final UserSecurity userSecurity;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command){
        RegisterResponse response = registrationService.register(command.username, command.getPassword());
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response.getErrorMessage());
        }
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, @AuthenticationPrincipal UserDetails user){
        String password = body.get("password");
        registrationService.changePassword(user, password);
        return ResponseEntity.noContent().build();
    }

    @Data
    static class RegisterCommand{
        @Email
        String username;
        @NotNull
        @Length(min = 6, max = 100, message = "Password must contain 6 to 100 characters")
        String password;
    }
}
