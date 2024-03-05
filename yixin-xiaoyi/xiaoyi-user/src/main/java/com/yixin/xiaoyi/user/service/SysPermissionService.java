package com.yixin.xiaoyi.user.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.core.domain.model.UserPermission;
import com.yixin.xiaoyi.common.core.domain.model.UserRoleMenuPermission;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.role.entity.XyMenu;
import com.yixin.xiaoyi.role.entity.XyRole;
import com.yixin.xiaoyi.role.service.XyMenuAdminService;
import com.yixin.xiaoyi.role.service.XyRoleAdminService;
import com.yixin.xiaoyi.user.dao.XyUserFinder;
import com.yixin.xiaoyi.user.entity.XyUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户权限处理
 *
 * @author admin
 */
@RequiredArgsConstructor
@Service
public class SysPermissionService {

    private final XyRoleAdminService roleService;
    private final XyMenuAdminService menuService;
    public final XyUserFinder xyUserFinder;


    /**
     * 刷新用户权限信息缓存
     * @param userId
     */
    public UserPermission refreshUserPermission(Long userId){
        UserPermission userPermission = buildPermission(userId);
        RedisUtils.setCacheObject(CacheConstants.PERMISSION+userPermission.getUserId(),userPermission, Duration.ofSeconds(86400));
        return userPermission;
    }


    /**
     * 获取用户权限信息
     * @param userId
     * @return
     */
    public UserPermission getUserPermission(Long userId){
        UserPermission userPermission = RedisUtils.getCacheObject(CacheConstants.PERMISSION+userId);
        if(ObjectUtil.isNotNull(userPermission)){
            return userPermission;
        }
        return refreshUserPermission(userId);
    }


    /**
     * 清空用户权限缓存
     * @param userId
     */
    public void deleteUserPermission(Long userId){
        RedisUtils.deleteObject(CacheConstants.PERMISSION+userId);
    }


    /**
     * 批量清空用户权限缓存
     * @param userIds
     */
    public void deleteUserPermissionBatch(List<Long> userIds){
        if(CollectionUtils.isEmpty(userIds)){
            return;
        }
        List<String> deleteKeys = new ArrayList<>();
        userIds.stream().forEach(userId -> {deleteKeys.add(CacheConstants.PERMISSION+userId);});
        RedisUtils.deleteObject(deleteKeys);
    }


    private  UserPermission buildPermission(Long userId){
        UserPermission userPermission = new UserPermission();
        userPermission.setUserId(userId);
        userPermission.setUserRoleMenuPermissions(getUserRoleMenuPermissions(userId));
        return userPermission;
    }

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(XyUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(XyUser user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            List<XyRole> roles = user.getRoles();
            if (!roles.isEmpty() && roles.size() > 1) {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (XyRole role : roles) {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }


    /**
     * 获取用户角色是对应的权限信息
     * @param userId
     * @return
     */
    private List<UserRoleMenuPermission> getUserRoleMenuPermissions(Long userId) {
        //获取用户未过期的角色
        List<XyRole> xyRoles = roleService.selectRolesByUserId(userId);
        List<Long> roleIds = xyRoles.stream().map(XyRole::getRoleId).collect(Collectors.toList());
        //获取角色对应的权限
        List<XyMenu> xyMenus = menuService.selectMenuPermsByRoles(roleIds);
        Map<Long,List<XyMenu>> roleIdAndPermissions = xyMenus.stream().collect(Collectors.groupingBy(XyMenu::getRoleId));
        //角色权限信息
        List<UserRoleMenuPermission> userRoleMenuPermissions = new ArrayList<>();
        for(XyRole xyRole:xyRoles){
            UserRoleMenuPermission userRoleMenuPermission = new UserRoleMenuPermission();
            userRoleMenuPermission.setRoleId(xyRole.getRoleId());
            userRoleMenuPermission.setRoleName(xyRole.getRoleName());
            userRoleMenuPermission.setRoleKey(xyRole.getRoleKey());
            userRoleMenuPermission.setExpireTime(xyRole.getRoleExpireTime());
            List<String> menuPerms = roleIdAndPermissions.get(xyRole.getRoleId()).stream().map(XyMenu::getPerms).distinct().collect(Collectors.toList());
            userRoleMenuPermission.setMenuPermission(new HashSet<>(menuPerms));
            userRoleMenuPermissions.add(userRoleMenuPermission);
        }
        return userRoleMenuPermissions;
    }

}
