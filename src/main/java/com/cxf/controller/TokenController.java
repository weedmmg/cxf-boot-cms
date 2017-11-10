package com.cxf.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cxf.base.BaseController;
import com.cxf.entity.UserEntity;
import com.cxf.enums.UserSexEnum;
import com.cxf.mapper.xrjf.UserMapper;
import com.cxf.util.StringHelper;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/auth")
public class TokenController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取TOKEN
     * 
     * @return
     */
    @RequestMapping
    public String token() {
        String token = "";
        token = getToken();
        if (StringHelper.nullOrBlank(token))
            token = "token not exist";
        return token;

    }

    /**
     * 核对Token
     * 
     * @param token
     * @return
     */
    @RequestMapping("check")
    public String check(@RequestParam("token") String token) {
        boolean b = checkToken(token);
        System.out.println(b + "@@" + token);
        return b + "";

    }

    /**
     * 登陆
     * 
     * @param q
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "login", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String login(String q) throws JsonProcessingException {
        resultMap = new HashMap<String, String>();
        try {
            Map<String, Object> paramMap = mapper.readValue(q, Map.class);
            String username = String.valueOf(paramMap.get("username")), password = String.valueOf(paramMap.get("password")), type = String.valueOf(paramMap.get("type"));

            if (StringHelper.nullOrBlank(type)) {
                type = "PWD";
            }
            if (StringHelper.nullOrBlank(username)) {
                return result(ERROR_CODE, "username:" + username + " is null.", null);
            }

            if (type.equals("PWD") && StringHelper.nullOrBlank(password)) {
                return result(ERROR_CODE, "username:" + username + " password:" + password + " is null.", null);
            }

            UserEntity user = userMapper.findByUsername(username);
            if (user == null) {
                return result(ERROR_CODE, "username:" + username + " not exist.", null);
            }

            if (type.equals("PWD") && (!user.getPassword().equals(password))) {
                return result(ERROR_CODE, "wrong password.", null);
            }

            Map<String, String> newMap = new HashMap<String, String>();
            newMap.put("userid", user.getId());
            newMap.put("token", login(username, user.getPassword()));
            return result(SUCCESS_CODE, SUCCESS_MSG, newMap);

        } catch (Exception e) {
            return result(ERROR_CODE, e.getMessage(), null);
        }

    }

    /**
     * 注册
     * 
     * @param q
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "register", produces = "application/json; charset=utf-8")
    public String register(String q) throws JsonProcessingException {
        try {
            Map<String, Object> paramMap = mapper.readValue(q, Map.class);
            String username = String.valueOf(paramMap.get("username")), password = String.valueOf(paramMap.get("password"));
            if (StringHelper.nullOrBlank(username) || StringHelper.nullOrBlank(password)) {
                return result(ERROR_CODE, "username:" + username + " password:" + password + " is null.", null);
            }

            UserEntity user = userMapper.findByUsername(username);
            if (user != null) {
                return result(ERROR_CODE, "username: exist.", null);
            }
            user = new UserEntity(username, password, UserSexEnum.MAN);

            userMapper.insert(user);

            Map<String, String> newMap = new HashMap<String, String>();

            user = userMapper.findByUsername(username);
            if (user != null) {
                newMap.put("userid", user.getId());
            }
            return result(SUCCESS_CODE, SUCCESS_MSG, newMap);

        } catch (Exception e) {
            return result(ERROR_CODE, e.getMessage(), null);
        }
    }

    /**
     * 注册
     * 
     * @param q
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "modify", produces = "application/json; charset=utf-8")
    public String modify(String q) throws JsonProcessingException {
        try {
            Map<String, Object> paramMap = mapper.readValue(q, Map.class);
            String username = String.valueOf(paramMap.get("username")), password = String.valueOf(paramMap.get("password")), oldpassword = String.valueOf(paramMap.get("oldpassword"));
            if (StringHelper.nullOrBlank(username) || StringHelper.nullOrBlank(password)) {
                return result(ERROR_CODE, "username:" + username + " password:" + password + " is null.", null);
            }

            UserEntity user = userMapper.findByUsername(username);
            if (user == null) {
                return result(ERROR_CODE, "username: " + username + " not exist.", null);
            }

            if (!StringHelper.nullOrBlank(oldpassword)) {
                // System.out.println(oldpassword + ":::" + user.getPassword());
                if (!oldpassword.equals(user.getPassword())) {
                    return result(ERROR_CODE, "oldpassword: " + oldpassword + " is not correct.", null);
                }
            }
            user.setPassword(password);
            userMapper.update(user);
            return result(SUCCESS_CODE, SUCCESS_MSG, null);

        } catch (Exception e) {
            return result(ERROR_CODE, e.getMessage(), null);
        }
    }

    /**
     * 登出
     * 
     * @param q
     * @return
     * @throws JsonProcessingException
     */
    // @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "logout", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String logout(String q) throws JsonProcessingException {

        try {
            Map<String, Object> paramMap = mapper.readValue(q, Map.class);
            String token = String.valueOf(paramMap.get("token"));
            if (StringHelper.nullOrBlank(token)) {
                return result(ERROR_CODE, "token:" + token + " is null.", null);
            }

            logout();
            return result(SUCCESS_CODE, SUCCESS_MSG, null);

        } catch (Exception e) {
            return result(ERROR_CODE, e.getMessage(), null);
        }
    }
}
