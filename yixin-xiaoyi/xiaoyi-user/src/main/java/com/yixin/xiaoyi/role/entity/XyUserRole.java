package com.yixin.xiaoyi.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 用户和角色关联 xy_user_role
 *
 * @author admin
 */

@Data
@TableName("xy_user_role")
public class XyUserRole extends BaseEntity {

    /**
     * 用户ID
     */
    @TableId(type = IdType.INPUT)
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;


    /**
     * VIP过期时间
     */
    private Date expireTime;

}
