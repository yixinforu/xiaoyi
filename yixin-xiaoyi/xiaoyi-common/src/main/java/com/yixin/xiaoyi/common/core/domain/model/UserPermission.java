package com.yixin.xiaoyi.common.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/9/1 22:08
 */

@Data
@NoArgsConstructor
public class UserPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 权限信息
     */
    private List<UserRoleMenuPermission> userRoleMenuPermissions;
}
