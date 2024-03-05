package com.yixin.xiaoyi.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: huangzexin
 * @date: 2023/10/7 11:49
 */
@Data
@Getter
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {


    /**
     * accessKeyId
     */
    @Getter
    private static String accessKeyId;
    /**
     * accessKeySecret
     */
    @Getter
    private static  String accessKeySecret;
    /**
     * openApi接入点
     */
    @Getter
    private static  String endpoint;

    /**
     * 短信签名
     */
    @Getter
    private static  String signName;

    /**
     * 短信模板
     */
    @Getter
    private static  String templateCode;

    /**
     * 短信模板内容
     */
    @Getter
    private static  String templateContent;

    /**
     * 规定时间（一小时）内同一手机号最多调用短信次数
     */
    @Getter
    private static String maxSmsCount;

    /**
     * 规定时间（一小时）内同一IP最多调用短信次数
     */
    @Getter
    private static String maxIpSmsCount;

    public  void setMaxIpSmsCount(String maxIpSmsCount) {
        SmsConfig.maxIpSmsCount = maxIpSmsCount;
    }

    public  void setMaxSmsCount(String maxSmsCount) {
        SmsConfig.maxSmsCount = maxSmsCount;
    }
    public  void setAccessKeyId(String accessKeyId) {
        SmsConfig.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        SmsConfig.accessKeySecret = accessKeySecret;
    }

    public void setEndpoint(String endpoint) {
        SmsConfig.endpoint = endpoint;
    }

    public void setSignName(String signName) {
        SmsConfig.signName = signName;
    }

    public void setTemplateCode(String templateCode) {
        SmsConfig.templateCode = templateCode;
    }

    public void setTemplateContent(String templateContent) {
        SmsConfig.templateContent = templateContent;
    }
}
