package com.yixin.xiaoyi.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yixin.xiaoyi.common.annotation.Sensitive;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import com.yixin.xiaoyi.common.enums.SensitiveStrategy;
import com.yixin.xiaoyi.common.xss.Xss;
import com.yixin.xiaoyi.role.entity.XyRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户对象 xy_user
 *
 * @author admin
 */

@Data
@NoArgsConstructor
@TableName("xy_user")
public class XyUser  extends BaseEntity  {

    /**
     * 用户ID
     */
    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;

    /**
     * 加密手机号
     */
    private String encryptedPhone;

    /**
     * 脱敏手机号
     */
    private String maskPhone;


    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;


    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;


    @TableField(exist = false)
    private String phone;



    /**
     * 角色对象
     */
    @TableField(exist = false)
    private List<XyRole> roles;

    /**
     * 角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;


    /**
     * 数据权限 当前角色ID
     */
    @TableField(exist = false)
    private Long roleId;



    public XyUser(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.userId);
    }

}
