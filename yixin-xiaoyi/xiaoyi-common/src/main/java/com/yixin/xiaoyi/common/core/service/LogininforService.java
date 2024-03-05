package com.yixin.xiaoyi.common.core.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用 系统访问日志
 *
 * @author admin
 */
public interface LogininforService {

    void recordLogininfor(String encryptedPhone,String maskPhone, String status, String message,
                          HttpServletRequest request, Object... args);
}
