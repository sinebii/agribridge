package com.agribridge.security;
import com.agribridge.exception.CustomUserNotActivatedException;
import com.agribridge.exception.CustomUserNotFoundException;
import com.agribridge.model.User;
import com.agribridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(()->new CustomUserNotFoundException(HttpStatus.NOT_FOUND, "User not found with username or email : "+usernameOrEmail));


        if(!user.getEnabled()){
            throw new CustomUserNotActivatedException(HttpStatus.LOCKED,"Account Not Activated, resend verification code : "+usernameOrEmail);
        }

        Set<GrantedAuthority> authorities =user.getRoles().stream()
                .map(role->new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
