package com.cxf.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cxf.base.BaseController;
import com.cxf.entity.UserEntity;
import com.cxf.third.sms.util.SendSMSUtil;
import com.cxf.util.StringHelper;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @Autowired
    private SendSMSUtil smsUtil;

    @RequestMapping
    public ModelAndView index(UserEntity user) {
        // result.addObject("user", new UserEntity());

        result.setViewName("index");
        return result;
    }

    @RequestMapping(value = "sms", produces = "application/json; charset=utf-8")
    public String msg(String q) throws JsonProcessingException {
        try {
            Map<String, Object> paramMap = mapper.readValue(q, Map.class);
            String phone = String.valueOf(paramMap.get("phone")), content = String.valueOf(paramMap.get("content"));
            if (StringHelper.nullOrBlank(phone) || StringHelper.nullOrBlank(content)) {
                return result(ERROR_CODE, "phone:" + phone + " content:" + content + " is null.", null);
            }

            smsUtil.sendSingleMessage(content, phone);

            return result(SUCCESS_CODE, SUCCESS_MSG, null);

        } catch (Exception e) {
            return result(ERROR_CODE, e.getMessage(), null);
        }

    }
}
