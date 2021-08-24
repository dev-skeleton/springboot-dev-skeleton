package com.example.skeleton.component.security;

import com.example.skeleton.model.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//自定义的验证类
@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationManager(UserDetailsService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var ud = userDetailService.loadUserByUsername(authentication.getName());
        assert ud instanceof User;
        if (passwordEncoder.matches((String) authentication.getCredentials(), ud.getPassword())) {
            var auth = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
            auth.setDetails(ud);
            return auth;
        }
        return null;
    }
}
