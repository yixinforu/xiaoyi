package com.yixin.xiaoyi.user.entity;

import lombok.Data;

/**
 * 当前在线会话
 *
 * @author admin
 */

@Data
public class XyUserOnline {

    /**
     * 会话编号
     */
    private String tokenId;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 脱敏手机
     */
    private String maskPhone;

    /**
     * 加密手机号
     */
    private String encryptedPhone;


    /**
     * 手机号
     */
    private String phone;

}
