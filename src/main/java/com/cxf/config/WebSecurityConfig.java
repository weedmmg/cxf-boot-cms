package com.cxf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cxf.secruity.JwtAuthenticationEntryPoint;
import com.cxf.secruity.JwtAuthenticationProvider;
import com.cxf.secruity.JwtAuthenticationTokenFilter;
import com.cxf.secruity.JwtUserDetailsServiceImpl;

/**
 * 配置整体站点安全
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    UserDetailsService jwtUserDetailsServiceImpl() { // 注册UserDetailsService
                                                     // 的bean
        return new JwtUserDetailsServiceImpl();
    }

    @Bean
    AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        AuthenticationProvider ap = jwtAuthenticationProvider();
        // ap.setPasswordEncoder(passwordEncoder());
        authenticationManagerBuilder.authenticationProvider(ap);
        // authenticationManagerBuilder.authenticationProvider(authenticationProvider)
        // authenticationManagerBuilder.userDetailsService(jwtUserDetailsServiceImpl()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * If subclassed this will potentially override subclass
     * configure(HttpSecurity)
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/**").permitAll();

        // http.logout().logoutSuccessUrl("/index"); // 退出默认跳转页面 (5)
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        http.headers().cacheControl();
    }

}
