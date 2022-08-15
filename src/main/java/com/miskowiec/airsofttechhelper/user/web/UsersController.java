package com.miskowiec.airsofttechhelper.user.web;

import com.miskowiec.airsofttechhelper.user.application.port.UserRegistrationUseCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import static com.miskowiec.airsofttechhelper.user.application.port.UserRegistrationUseCase.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UserRegistrationUseCase registrationService;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command){
        RegisterResponse response = registrationService.register(command.username, command.getPassword());
        if(!response.isSuccess()){
            return ResponseEntity.badRequest().body(response.getErrorMessage());
        }
        return ResponseEntity.accepted().build();
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
