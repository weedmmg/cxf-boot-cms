package com.cxf.secruity;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cxf.entity.UserEntity;

final class JwtUserFactory {

    private JwtUserFactory() {
    }

    static JwtUser create(UserEntity user) {
        return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), mapToGrantedAuthorities(user.getRoles()), user.getLastPasswordResetDate());
    }

    private static Set<GrantedAuthority> mapToGrantedAuthorities(Set<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
