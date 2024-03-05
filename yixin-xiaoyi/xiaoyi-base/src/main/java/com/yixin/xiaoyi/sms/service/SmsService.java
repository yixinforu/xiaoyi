package com.yixin.xiaoyi.sms.service;

import com.yixin.xiaoyi.sms.model.vo.SmsVO;

/**
 * @author: huangzexin
 * @date: 2023/10/6 22:18
 */
public interface SmsService {





    /**
     * 异步发送短信
     * @param phone
     * @param code
     * @return
     */
    public void asyncSendSmsMessage(String phone, String code);
}
