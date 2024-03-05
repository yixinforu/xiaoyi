package com.yixin.xiaoyi.user.model.dto;

import com.yixin.xiaoyi.role.entity.XyRole;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/28 15:12
 */
@Data
public class UserDTO {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phone;


    /**
     * 角色信息
     */
    private List<XyRole> roles;


    /**
     * 最近活跃时间
     */
    private Date lastActiveTime;

    /**
     * 最近活跃ip
     */
    private String lastActiveIp;


    /**
     * 注册时间
     */
    private Date createTime;


    /**
     * 是否被封禁
     */
    private Boolean isDisabled;


    /**
     * 封禁时间
     */
    private Date disableTime;

}
