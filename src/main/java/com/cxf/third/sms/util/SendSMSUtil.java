package com.cxf.third.sms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cxf.third.sms.request.SmsSendRequest;
import com.cxf.third.sms.request.SmsVariableRequest;
import com.cxf.third.sms.response.SmsSendResponse;
import com.cxf.third.sms.response.SmsVariableResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 短信处理类
 * 
 * @author ZENGCHONG
 * @date 2017-9-19
 */
@Component
public class SendSMSUtil {

    Logger log = LoggerFactory.getLogger(SendSMSUtil.class);

    protected ObjectMapper mapper = new ObjectMapper();
    /**
     * 短信平台账号
     */
    @Value("${msg.userName}")
    private String ACCOUNT;

    /**
     * 短信平台 密码
     */
    @Value("${msg.password}")
    private String PAESSWORD;

    /**
     * 单发
     */
    @Value("${msg.single.send.url}")
    private String MSG_SINGLE_SEND_URL;

    /**
     * 群发
     */
    @Value("${msg.mores.send.url}")
    private String MSG_MORES_SEND_URL;

    /**
     * 状态报告
     */
    private String REPORT = "true";

    /**
     * 发送单条短信
     * 
     * @author ZENGCHONG
     * @param content
     *            短信类容
     * @param phone
     *            接收手机号码
     * @return
     */
    public SmsSendResponse sendSingleMessage(String content, String phone) {
        SmsSendResponse smsSingleResponse = new SmsSendResponse();
        try {
            SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PAESSWORD, content, phone, REPORT);

            String requestJson = mapper.writeValueAsString(smsSingleRequest);

            // JSON.toJSONString(smsSingleRequest);

            log.info("before request string is: " + requestJson);

            String response = SmsUtil.sendSmsByPost(MSG_SINGLE_SEND_URL, requestJson);

            log.info("response after request result is :" + response);

            smsSingleResponse = mapper.readValue(response, SmsSendResponse.class);
            // JSON.parseObject(response, SmsSendResponse.class);

            return smsSingleResponse;
        } catch (Exception e) {
            log.error("发送单条短信异常" + e.getMessage());
        }
        return smsSingleResponse;
    }

    /**
     * 群发短信接口
     * 
     * @author ZENGCHONG
     * @param content
     *            短信类容
     * @param params
     *            参数，多手机号码请用;分割 186xxxx;186xxxx
     * @return
     */
    public SmsVariableResponse sendMoresMessage(String content, String params) {
        SmsVariableResponse smsVariableResponse = new SmsVariableResponse();
        try {
            SmsVariableRequest smsVariableRequest = new SmsVariableRequest(ACCOUNT, PAESSWORD, content, params, REPORT);

            String requestJson = mapper.writeValueAsString(smsVariableRequest);
            // JSON.toJSONString(smsVariableRequest);

            log.info("sendMoresMessage before request string is: " + requestJson);

            String response = SmsUtil.sendSmsByPost(MSG_MORES_SEND_URL, requestJson);
            System.out.println(response);
            log.info("sendMoresMessage after request result is : " + response);

            smsVariableResponse = mapper.readValue(response, SmsVariableResponse.class);
            // JSON.parseObject(response, SmsVariableResponse.class);

            return smsVariableResponse;
        } catch (Exception e) {
            log.error("群发短信异常" + e.getMessage());
        }
        return smsVariableResponse;
    }
}
