package com.cxf.controller;

import java.security.Principal;

import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cxf.base.BaseController;
import com.cxf.entity.UserEntity;
import com.cxf.mapper.xrjf.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;

/**
 * 
 * @Date: 2017年10月23日 下午1:31:40
 * @author chenxf
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping
    public ModelAndView list(String userName, String page) {
        int pageNum = 0;
        if (!StringHelper.isNullOrEmptyString(page)) {
            pageNum = Integer.valueOf(page);
        }
        Page<UserEntity> pageInfo = PageHelper.startPage(pageNum, PAGESIZE);
        // PageHelper会自动拦截到下面这查询sql
        this.userMapper.getAll(userName);
        // result.addObject("pageInfo", new PageInfo<Country>(countryList));
        result.addObject("page", pageInfo.toPageInfo());
        result.setViewName("user/list");
        result.addObject("userName", userName);
        return result;
    }

    @RequestMapping("hello")
    @ResponseBody
    public String helloWorld(Principal principal) {
        return principal == null ? "Hello anonymous" : "Hello " + principal.getName() + " jj:";
    }

    @RequestMapping("add")
    public ModelAndView add(UserEntity user) {
        result.addObject("user", new UserEntity());

        result.setViewName("user/add");
        return result;
    }

    @RequestMapping("save")
    public ModelAndView save(UserEntity user) {
        log.info("this is save");
        if (Strings.isNullOrEmpty(user.getId())) {

            userMapper.insert(user);
        } else {
            userMapper.update(user);
        }
        result.setViewName("redirect:/users/");
        return result;

    }

    @RequestMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        userMapper.delete(id);
        result.setViewName("redirect:/users/");
        return result;
    }

}
