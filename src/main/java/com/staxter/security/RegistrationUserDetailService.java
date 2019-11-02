package com.staxter.security;

import com.staxter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * RegistrationUserDetailService is injected in the Web Security configuration, it's used during authentication process to check if the user exists
 *
 * @author Julian Vasa
 */
@Component(value = "userDetailService")
public class RegistrationUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /* Check if username exists in the local storage */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.staxter.model.User user = userRepository.findByUsername(username);
        return new User(String.valueOf(Objects.requireNonNull(user).getUserName()), user.getHashedPassword(), getAuthority());
    }

    /* Assign a default role */
    private List<SimpleGrantedAuthority> getAuthority() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
