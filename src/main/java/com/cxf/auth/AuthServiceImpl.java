package com.cxf.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cxf.entity.UserEntity;
import com.cxf.mapper.xrjf.UserMapper;
import com.cxf.secruity.JwtTokenUtil;
import com.cxf.secruity.JwtUser;
import com.cxf.util.PasswordEncoderGenerator;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private UserMapper userService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, UserMapper userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public UserEntity register(UserEntity userToAdd) {
        final String username = userToAdd.getUsername();
        if (userService.findByUsername(username) != null) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));
        userToAdd.setLastPasswordResetDate(new Date());
        userService.insert(userToAdd);
        return userToAdd;
    }

    @Override
    public String login(String username, String password) {

        /**
         * UsernamePasswordAuthenticationToken upToken = new
         * UsernamePasswordAuthenticationToken(username, password); //
         * upToken.setDetails(details); // authentication.setDetails(new //
         * WebAuthenticationDetailsSource().buildDetails(request));
         * System.out.println("authenticated user " + username +
         * ", setting security context");
         * 
         * SecurityContextHolder.getContext().setAuthentication(upToken);
         */

        String rawPassword = PasswordEncoderGenerator.encodePassword(password);
        System.out.println("rawPassword:" + rawPassword);
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, rawPassword); // 进行安全认证
        // final Authentication authentication =
        // authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(upToken);
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        upToken.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(upToken);
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
