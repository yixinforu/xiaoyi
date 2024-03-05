package com.yixin.xiaoyi.sms.dao;

import com.yixin.xiaoyi.sms.entity.SmsSendLog;
import com.yixin.xiaoyi.sms.dao.mapper.SmsSendLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author: huangzexin
 * @date: 2023/10/6 21:33
 */
@Repository
@RequiredArgsConstructor
public class SmsSendLogFinder {

    private final SmsSendLogMapper smsSendLogMapper;

    public int insertSmsSendLog(SmsSendLog smsSendLog){
        return smsSendLogMapper.insert(smsSendLog);
    }
}
