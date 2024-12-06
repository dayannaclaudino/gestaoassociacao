package com.api.gestaoassociacao.service;

import lombok.AllArgsConstructor;

import com.api.gestaoassociacao.model.User;
import com.api.gestaoassociacao.repository.UserRepository;
import com.api.gestaoassociacao.security.IUserService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService, IUserService {

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;    

    @Override
    public Long saveUser(User user) {
            String passwd= user.getPassword();
            String encodedPasswod = passwordEncoder.encode(passwd);
            user.setPassword(encodedPasswod);
            user = userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não existe."));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,
                user.getPassword(),
                authorities
        );
    }
    
}
