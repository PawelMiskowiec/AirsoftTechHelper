package com.miskowiec.airsofttechhelper.user.application;

import com.miskowiec.airsofttechhelper.user.application.port.UserRegistrationUseCase;
import com.miskowiec.airsofttechhelper.user.db.UserEntityRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
