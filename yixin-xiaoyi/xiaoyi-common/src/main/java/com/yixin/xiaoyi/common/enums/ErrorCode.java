package com.yixin.xiaoyi.common.enums;


/**
 * @author: yixin
 * @date: 2023/2/19 15:52
 */
public enum ErrorCode {
    //共用模块
    SUCCESS(0,"OK"),
    USER_UNAUTHORIZED(401,"UNAUTHORIZED"),
    USER_DATA_UNAUTHORIZED(5001,"DATA_UNAUTHORIZED"),
    SYSTEM_ERROR(50000,"系统内部异常"),
    VIOLATION_CALL(5002,"违规调用"),
    //用户模块
    USER_LOGIN_FAIL(60001,"用户登录失败"),
    USER_REGISTER_FAIL(60002,"用户注册失败"),
    USER_LOGOUT_FAIL(60003,"用户注销失败"),
    USER_NOT_FOUND(60004,"查无此用户"),
    USER_NOT_INVITE_CODE(60005,"用户无对应邀请码"),
    SMS_ERROR_CODE(60006,"验证码错误"),
    SMS_INVALID_CODE(60007,"验证码已失效"),
    USER_PHONE_FAIL(60008,"手机号码格式错误"),

    //招聘模块
    RECRUIT_NOT_FOUND(70001,"查无此招聘信息"),
    EMPOWER_FAIL(70002,"授权失败"),

    //短信模块
    SMS_PHONE_REPEAT_MAX_COUNT(80002,"短信次数已达限制，请稍后再试"),
    SMS_IP_REPEAT_MAX_COUNT(80003,"短信调用过于频繁，请稍后再试");





    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;

    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
