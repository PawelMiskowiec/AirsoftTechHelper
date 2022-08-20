package com.miskowiec.airsofttechhelper.user.web;

import com.miskowiec.airsofttechhelper.security.UserSecurity;
import com.miskowiec.airsofttechhelper.user.application.port.UserUseCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintDefinitionException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.miskowiec.airsofttechhelper.user.application.port.UserUseCase.RegisterResponse;

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
    public ResponseEntity<?> resetPassword(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails user
    ){
        String password = body.get("password");
        validate(password);
        registrationService.changePassword(user, password);
        return ResponseEntity.noContent().build();
    }

    private void validate(String password){
        if(password.length() < 6 || password.length() > 100){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain 6 to 100 characters"
            );
        }
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
