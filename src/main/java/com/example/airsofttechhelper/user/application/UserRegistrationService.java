package com.example.airsofttechhelper.user.application;

import com.example.airsofttechhelper.user.application.port.UserRegistrationUseCase;
import com.example.airsofttechhelper.user.db.UserEntityRepository;
import com.example.airsofttechhelper.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class UserRegistrationService implements UserRegistrationUseCase {

    private final UserEntityRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public RegisterResponse register(String username, String password) {
        if(repository.findByUsername(username).isPresent()){
            return new RegisterResponse(false, "Account already exists");
        }
        UserEntity user = new UserEntity(username, encoder.encode(password));
        repository.save(user);
        return RegisterResponse.SUCCESS;
    }
}
