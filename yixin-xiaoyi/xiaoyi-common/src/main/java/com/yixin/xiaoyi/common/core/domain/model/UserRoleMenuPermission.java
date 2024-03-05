package com.yixin.xiaoyi.common.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author: huangzexin
 * @date: 2023/9/1 22:11
 */

@Data
@NoArgsConstructor
public class UserRoleMenuPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色Id
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

    /**
     * 过期时间
     */
    private Date expireTime;


    /**
     * 角色菜单权限
     */
    private Set<String> menuPermission;
}
