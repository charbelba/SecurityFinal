package com.example.SecurityFinal.Api.Service;

import com.example.SecurityFinal.Api.Entity.CustomUserDetails;
import com.example.SecurityFinal.Api.Exception.UserNotFoundException;
import com.example.SecurityFinal.Api.Repository.RoleRepository;
import com.example.SecurityFinal.Api.Repository.UserRepository;
import com.example.SecurityFinal.Api.Entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        return new CustomUserDetails(user);
    }
}