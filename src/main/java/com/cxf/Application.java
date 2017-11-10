package com.cxf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @Date: 2017年10月23日 下午1:18:56
 * @author chenxf
 */
@Controller
@EnableWebMvc
// @EnableDiscoveryClient
@EnableWebSecurity
@ComponentScan({ "com.cxf" })
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

    private static final Class<Application> applicationClass = Application.class;
    private static final Logger log = LoggerFactory.getLogger(applicationClass);

    public static void main(String[] args) {

        SpringApplication.run(applicationClass, args);

    }

}
