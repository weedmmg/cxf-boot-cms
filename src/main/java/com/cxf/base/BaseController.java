package com.cxf.base;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.ModelAndView;

import com.cxf.secruity.JwtTokenUtil;
import com.cxf.secruity.JwtUser;
import com.cxf.util.PasswordEncoderGenerator;
import com.cxf.util.StringHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @Date: 2017年10月23日 下午2:52:50
 * @author chenxf
 */
public class BaseController {

    protected ModelAndView result = new ModelAndView();

    protected int PAGESIZE = 8;

    protected String SUCCESS_CODE = "0";
    protected String ERROR_CODE = "-1";

    protected String SUCCESS_MSG = "成功";

    protected static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    protected Map<String, String> resultMap = new HashMap<String, String>();

    protected ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取当前用户Token
     * 
     * @return
     */
    protected String getToken() {
        SecurityContext sc = SecurityContextHolder.getContext();
        Object principal = sc.getAuthentication().getDetails();
        System.out.println("token:::" + principal.toString());

        if (principal instanceof JwtUser) {
            JwtUser user = (JwtUser) principal;

            return jwtTokenUtil.generateToken(user);
        }

        return null;
    }

    protected boolean checkToken(String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);

            if (StringHelper.nullOrBlank(username)) {
                return false;
            }
            if (jwtTokenUtil.isTokenExpired(token)) {
                return false;
            }
            return true;
        } catch (UsernameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public String result(String code, String message, Map<String, String> paramMap) throws JsonProcessingException {
        resultMap.put("code", code);
        resultMap.put("msg", message);
        if (paramMap != null) {
            resultMap.putAll(paramMap);
        }
        return mapper.writeValueAsString(resultMap);
    }

    public String login(String username, String password) {

        String rawPassword = PasswordEncoderGenerator.encodePassword(password);
        // System.out.println("rawPassword:" + rawPassword);

        // SecurityContextHolder.getContext().setAuthentication(upToken);

        // 生成token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, rawPassword, userDetails.getAuthorities()); // 进行安全认证
        upToken.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(upToken);
        return jwtTokenUtil.generateToken(userDetails);
    }

    public void logout() {

        SecurityContextHolder.clearContext();
    }
}
