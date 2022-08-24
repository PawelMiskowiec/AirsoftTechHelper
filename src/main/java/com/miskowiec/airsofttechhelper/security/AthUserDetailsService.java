package com.miskowiec.airsofttechhelper.security;

import com.miskowiec.airsofttechhelper.user.db.UserEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class AthUserDetailsService implements UserDetailsService {

    private final UserEntityJpaRepository userEntityJpaRepository;
    private final AdminConfig adminConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(adminConfig.getUsername().equalsIgnoreCase(username)){
            return adminConfig.adminUser();
        }
        return userEntityJpaRepository.findByUsername(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
