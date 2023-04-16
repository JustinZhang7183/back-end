package com.justin.backend.springsecuritydemo.configuration.providers;

import com.justin.backend.springsecuritydemo.configuration.CustomAuthentication;
import com.justin.backend.springsecuritydemo.configuration.SecurityUser;
import com.justin.backend.springsecuritydemo.service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class CustomNeedPasswordAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;

    private final MyUserDetailsService userDetailsService;

    public CustomNeedPasswordAuthenticationProvider(PasswordEncoder passwordEncoder, MyUserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomNeedPasswordAuthenticationProvider authenticate");
        var authRequest = (CustomAuthentication) authentication;
        var username = authRequest.getName();
        var password = authRequest.getPassword();
        if (!"admin".equals(username)) {
            return null;
        }
        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(username);
        user.setBirthDate(null);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials!");
        }
        return CustomAuthentication.authenticated(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.isAssignableFrom(authentication);
    }
}
