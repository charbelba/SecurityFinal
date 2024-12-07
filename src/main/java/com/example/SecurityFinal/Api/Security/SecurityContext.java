package com.example.SecurityFinal.Api.Security;

import com.example.SecurityFinal.Api.Entity.UserInfo;
import com.example.SecurityFinal.Api.Exception.UserNotFoundException;
import com.example.SecurityFinal.Api.Repository.UserRepository;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class SecurityContext {

    private  final UserRepository userRepository;

    private SecurityContext(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public  UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new UserNotFoundException("User not found");
        }
        System.out.println(authentication.getPrincipal().getClass());
        String email = userDetails.getUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
