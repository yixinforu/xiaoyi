package com.yixin.xiaoyi.common.core.module.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色
 *
 * @author admin
 */

@Data
@NoArgsConstructor
public class RoleDTO implements Serializable {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;



}
