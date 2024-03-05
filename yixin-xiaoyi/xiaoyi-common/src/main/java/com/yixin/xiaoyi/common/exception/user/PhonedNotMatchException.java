package com.yixin.xiaoyi.common.exception.user;

/**
 * 用户手机号不符合规范异常类
 *
 * @author admin
 */
public class PhonedNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public PhonedNotMatchException() {
        super("user.password.not.match");
    }
}
