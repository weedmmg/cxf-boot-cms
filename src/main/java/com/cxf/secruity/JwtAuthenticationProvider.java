package com.cxf.secruity;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return new UsernamePasswordAuthenticationToken(username, password, authorities);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
