package com.yixin.xiaoyi.common.constant;

/**
 * 缓存的key 常量
 *
 * @author admin
 */
public interface CacheConstants {

    String USER_KEY_PREFIX = "xy-user:";

    /**
     * 权限
     */
    String AUTHORIZATION = "Authorization:";

    /**
     * 登录权限
     */
    String LOGIN = AUTHORIZATION+"login:";

    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY = LOGIN+"token:";

    /**
     * 在线用户 redis key
     */
    String ONLINE_TOKEN_KEY = USER_KEY_PREFIX+"online_tokens:";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = USER_KEY_PREFIX+"captcha_codes:";

    /**
     * 登录账户密码错误次数 redis key
     */
    String PWD_ERR_CNT_KEY = USER_KEY_PREFIX+"pwd_err_cnt:";


    /**
     * 校验码错误次数
     */
    String SMS_ERR_CNT_KEY = USER_KEY_PREFIX+"sms_err_cnt:";


    /**
     * 客户端在线请求日志
     */
    String CLIENT_ONLINE_LOG = USER_KEY_PREFIX+"client_online_log:";

    /**
     * 登录校验码
     */
    String SMS_LOGIN_CODE_KEY = USER_KEY_PREFIX+"sms_login_code:";


    String SMS_PHONE_LIMIT_KEY = USER_KEY_PREFIX+"sms_limit:phone:";

    String SMS_IP_LIMIT_KEY = USER_KEY_PREFIX+"sms_limit:ip:";





    /**
     * 用户权限信息
     */
    String PERMISSION = LOGIN+"permission:";

    /**
     * 参数管理 cache key
     */
    String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    String RATE_LIMIT_KEY = "rate_limit:";







}
