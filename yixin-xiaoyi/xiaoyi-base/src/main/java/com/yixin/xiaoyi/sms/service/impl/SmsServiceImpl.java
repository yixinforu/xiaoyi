package com.yixin.xiaoyi.sms.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.yixin.xiaoyi.common.config.SmsConfig;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.utils.EncryptionUtil;
import com.yixin.xiaoyi.sms.dao.SmsSendLogFinder;
import com.yixin.xiaoyi.sms.entity.SmsSendLog;
import com.yixin.xiaoyi.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: huangzexin
 * @date: 2023/10/6 22:18
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmsServiceImpl implements SmsService {

    private Client client;
    private final SmsSendLogFinder smsSendLogFinder;


    /**
     * 短信发送
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    @Async
    @Override
    public void asyncSendSmsMessage(String phone, String code){
        Client client = createClient();
        //封装参数
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
            .setPhoneNumbers(phone)
            .setSignName(SmsConfig.getSignName())
            .setTemplateCode(SmsConfig.getTemplateCode())
            .setTemplateParam("{\"code\":"+"\""+code+"\""+"}");
        //记录短信日志
        SmsSendLog smsSendLog = new SmsSendLog()
            .setEncryptedPhone(EncryptionUtil.encryptKey(phone, UserConfig.getPhoneSecret()))
            .setMaskPhone(DesensitizedUtil.mobilePhone(phone))
            .setSmsContent(String.format(SmsConfig.getTemplateContent(),code));
        try {
            SendSmsResponseBody responseBody = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions()).getBody();
            smsSendLog.setResultCode(responseBody.getCode())
                .setResultMsg(responseBody.getMessage())
                .setBizId(responseBody.getBizId());
        } catch (TeaException error) {
            // 如有需要，请打印 error
            smsSendLog.setResultCode("ERROR");
            smsSendLog.setResultMsg(error.getMessage());
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            smsSendLog.setResultCode("ERROR");
            smsSendLog.setResultMsg(_error.getMessage());
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        }finally {
            smsSendLogFinder.insertSmsSendLog(smsSendLog);
        }
    }

    /**
     * 创建Client对象
     * @return
     */
    public Client createClient(){
        if (client == null) {
            Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(SmsConfig.getAccessKeyId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(SmsConfig.getAccessKeySecret());
            // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
            config.endpoint = SmsConfig.getEndpoint();
            try {
                client = new Client(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return client;
    }



}
