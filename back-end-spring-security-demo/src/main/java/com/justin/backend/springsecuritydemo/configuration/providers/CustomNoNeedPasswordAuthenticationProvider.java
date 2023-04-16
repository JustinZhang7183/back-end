package com.justin.backend.springsecuritydemo.configuration.providers;

import com.justin.backend.springsecuritydemo.configuration.CustomAuthentication;
import com.justin.backend.springsecuritydemo.configuration.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

@Slf4j
public class CustomNoNeedPasswordAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomNoNeedPasswordAuthenticationProvider authenticate");
        var authRequest = (CustomAuthentication) authentication;
        var username = authRequest.getName();
        var password = authRequest.getPassword();
        if (!"admin".equals(username)) {
            return null;
        }
        SecurityUser user = new SecurityUser(username, password, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        return CustomAuthentication.authenticated(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.isAssignableFrom(authentication);
    }
}
