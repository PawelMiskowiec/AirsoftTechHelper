package com.miskowiec.airsofttechhelper.user.application;

import com.miskowiec.airsofttechhelper.user.application.port.UserUseCase;
import com.miskowiec.airsofttechhelper.user.db.UserEntityJpaRepository;
import com.miskowiec.airsofttechhelper.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UserService implements UserUseCase {
    private final UserEntityJpaRepository repository;
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

    @Transactional
    @Override
    public UpdatePasswordResponse changePassword(UserDetails user, String newPassword) {
        return repository.findByUsername(user.getUsername())
                .map(userEntity -> {
                    userEntity.setPassword(encoder.encode(newPassword));
                    return UpdatePasswordResponse.SUCCESS;
                })
                .orElse( new UpdatePasswordResponse(false, "Cannot find " + user.getUsername() + " user"));
    }

}
