package com.yixin.xiaoyi.framework.satoken.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.core.domain.model.LoginUser;
import com.yixin.xiaoyi.common.core.domain.model.UserPermission;
import com.yixin.xiaoyi.common.core.domain.model.UserRoleMenuPermission;
import com.yixin.xiaoyi.common.enums.UserType;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.DateUtils;
import com.yixin.xiaoyi.user.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * sa-token 权限管理实现类
 *
 * @author admin
 */
@Component
@RequiredArgsConstructor
public class SaPermissionImpl implements StpInterface {

    private final SysPermissionService permissionService;

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //获取未过期的权限
        List<UserRoleMenuPermission> userRoleMenuPermissions = getNotExpirePermission();
        List<String> menuPermissions = new ArrayList<>();
        for(UserRoleMenuPermission userRoleMenuPermission:userRoleMenuPermissions){
            menuPermissions.addAll(userRoleMenuPermission.getMenuPermission());
        }
        return menuPermissions;
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        //获取未过期的权限
        List<UserRoleMenuPermission> userRoleMenuPermissions = getNotExpirePermission();
        List<String> rolePermissions = new ArrayList<>();
        for(UserRoleMenuPermission userRoleMenuPermission:userRoleMenuPermissions){
            rolePermissions.add(userRoleMenuPermission.getRoleKey());
        }
        return rolePermissions;
    }

    private  List<UserRoleMenuPermission> getNotExpirePermission(){
        UserPermission userPermission = permissionService.getUserPermission(StpUtil.getLoginIdAsLong());
        //获取未过期的权限
        List<UserRoleMenuPermission> userRoleMenuPermissions = userPermission.getUserRoleMenuPermissions()
            .stream().filter(permission -> ObjectUtil.isNull(permission.getExpireTime()) || permission.getExpireTime().compareTo(new Date())>0).collect(Collectors.toList());
        return userRoleMenuPermissions;
    }
}
