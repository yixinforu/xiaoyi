package com.yixin.xiaoyi.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: huangzexin
 * @date: 2023/10/8 21:43
 */
@Data
@Getter
@Component
@ConfigurationProperties(prefix = "user")
public class UserConfig {


    /**
     * 普通角色id
     */
    @Getter
    private static Long baseRoleId;

    /**
     * vip id
     */
    @Getter
    private static Long vipRoleId;

    /**
     * 手机密钥
     */
    @Getter
    private static String phoneSecret;

    /**
     * 用户缓存访问过期时间 单位：小时
     */
    @Getter
    private static Integer accessLogExpireTime;

    /**
     * 最大允许登录失败次数
     */
    @Getter
    private static Integer maxRetryCount;

    /**
     * 登录失败锁定时间 单位：分钟
     */
    @Getter
    private static Integer lockTime;


    public  void setBaseRoleId(Long baseRoleId) {
        UserConfig.baseRoleId = baseRoleId;
    }

    public  void setVipRoleId(Long vipRoleId) {
        UserConfig.vipRoleId = vipRoleId;
    }

    public  void setPhoneSecret(String phoneSecret) {
        UserConfig.phoneSecret = phoneSecret;
    }

    public  void setAccessLogExpireTime(Integer accessLogExpireTime) {
        UserConfig.accessLogExpireTime = accessLogExpireTime;
    }

    public void setLockTime(Integer lockTime) {
        UserConfig.lockTime = lockTime;
    }

    public void setMaxRetryCount(Integer maxRetryCount) {
        UserConfig.maxRetryCount = maxRetryCount;
    }
}
