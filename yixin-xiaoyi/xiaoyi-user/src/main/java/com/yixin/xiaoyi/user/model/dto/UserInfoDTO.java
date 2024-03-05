package com.yixin.xiaoyi.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/9/21 11:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 脱敏手机号
     */
    private String maskedPhone;

    /**
     * 权限标识
     */
    private List<String> permissions;

    /**
     * VIP状态 0:非vip 1：vip
     */
    private String vipStatus;

    /**
     * 到期时间
     */
    private Date expireTime;

}
