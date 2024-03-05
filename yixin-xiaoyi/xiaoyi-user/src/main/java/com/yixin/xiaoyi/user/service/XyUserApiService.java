package com.yixin.xiaoyi.user.service;

import com.yixin.xiaoyi.sms.model.vo.SmsVO;
import com.yixin.xiaoyi.user.model.dto.UserInfoDTO;

/**
 * @author: huangzexin
 * @date: 2023/9/21 14:15
 */
public interface XyUserApiService {


    /**
     * 获取用户基本信息
     * @return
     */
    UserInfoDTO getUserInfo();

    /**
     * 发送短信验证码
     * @param smsVO
     * @return
     */
    public void sendSmsCode(SmsVO smsVO);


}
