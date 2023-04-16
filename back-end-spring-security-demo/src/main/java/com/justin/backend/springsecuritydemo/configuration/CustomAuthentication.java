package com.justin.backend.springsecuritydemo.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class CustomAuthentication implements Authentication {
    private final boolean isAuthenticated;
    private final String name;
    private final String password;
    private final SecurityUser securityUser;
    private final Collection<GrantedAuthority> authorities;

    private CustomAuthentication(Collection<GrantedAuthority> authorities, String name, SecurityUser securityUser, String password) {
        this.authorities = authorities;
        this.name = name;
        this.password = password;
        this.securityUser = securityUser;
        this.isAuthenticated = password == null;
    }

    public static CustomAuthentication unauthenticated(String name, String password) {
        return new CustomAuthentication(Collections.emptyList(), name, null, password);
    }

    public static CustomAuthentication authenticated(SecurityUser securityUser) {
        return new CustomAuthentication(securityUser.getAuthorities(), securityUser.getUsername(), securityUser, null);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return securityUser;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Don't do this"); // TODO: why?
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
