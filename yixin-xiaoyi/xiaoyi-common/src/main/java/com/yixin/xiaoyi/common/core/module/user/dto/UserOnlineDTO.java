package com.yixin.xiaoyi.common.core.module.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 当前在线会话
 *
 * @author admin
 */

@Data
@NoArgsConstructor
public class UserOnlineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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



}
