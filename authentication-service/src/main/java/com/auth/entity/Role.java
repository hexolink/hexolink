package com.auth.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_ANONYMOUS;

    @Override
    public String getAuthority() {
        return name();
    }
}
