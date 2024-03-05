package com.yixin.xiaoyi.common.core.service;

import com.yixin.xiaoyi.common.core.module.user.dto.OperLogDTO;
import org.springframework.scheduling.annotation.Async;

/**
 * 通用 操作日志
 *
 * @author admin
 */
public interface OperLogService {

    @Async
    void recordOper(OperLogDTO operLogDTO);
}
