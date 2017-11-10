package com.cxf.secruity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cxf.entity.UserEntity;
import com.cxf.mapper.xrjf.UserMapper;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);
    @Autowired
    private UserMapper userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("user is " + username);
        logger.info("Authenticating cxfuser with userName={}", username);
        UserEntity user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            user.addRole("ROLE_USER");
            return JwtUserFactory.create(user);
        }
    }
}
