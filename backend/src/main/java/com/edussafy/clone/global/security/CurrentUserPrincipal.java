package com.edussafy.clone.global.security;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record CurrentUserPrincipal(
        Long userId,
        String email,
        UserRole role
) {
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
