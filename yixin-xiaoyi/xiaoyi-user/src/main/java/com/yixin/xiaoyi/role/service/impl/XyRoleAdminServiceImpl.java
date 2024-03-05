package com.yixin.xiaoyi.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.role.dao.XyRoleFinder;
import com.yixin.xiaoyi.role.dao.XyRoleMenuFinder;
import com.yixin.xiaoyi.role.dao.XyUserRoleFinder;
import com.yixin.xiaoyi.role.entity.XyRole;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.role.entity.XyRoleMenu;
import com.yixin.xiaoyi.role.entity.XyUserRole;
import com.yixin.xiaoyi.role.service.XyRoleAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author admin
 */
@RequiredArgsConstructor
@Service
public class XyRoleAdminServiceImpl implements XyRoleAdminService {

    private final XyRoleFinder xyRoleFinder;
    private final XyUserRoleFinder xyUserRoleFinder;
    private final XyRoleMenuFinder xyRoleMenuFinder;

    @Override
    public TableDataInfo<XyRole> selectPageRoleList(XyRole role, PageQuery pageQuery) {
        Page<XyRole> page = xyRoleFinder.selectPageRoleList(role,pageQuery);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<XyRole> selectRoleList(XyRole role) {
        return xyRoleFinder.selectRoleList(role);
    }

    private Wrapper<XyRole> buildQueryWrapper(XyRole role) {
        Map<String, Object> params = role.getParams();
        QueryWrapper<XyRole> wrapper = Wrappers.query();
        wrapper.eq("r.del_flag", UserConstants.ROLE_NORMAL)
            .eq(ObjectUtil.isNotNull(role.getRoleId()), "r.role_id", role.getRoleId())
            .like(StringUtils.isNotBlank(role.getRoleName()), "r.role_name", role.getRoleName())
            .eq(StringUtils.isNotBlank(role.getStatus()), "r.status", role.getStatus())
            .like(StringUtils.isNotBlank(role.getRoleKey()), "r.role_key", role.getRoleKey())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                "r.create_time", params.get("beginTime"), params.get("endTime"))
            .orderByAsc("r.role_sort");
        return wrapper;
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<XyRole> selectRolesByUserId(Long userId) {
        return xyRoleFinder.selectRolePermissionByUserId(userId);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<XyRole> perms = xyRoleFinder.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (XyRole perm : perms) {
            if (ObjectUtil.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<XyRole> selectRoleAll() {
        return this.selectRoleList(new XyRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return xyRoleFinder.selectRoleListByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public XyRole selectRoleById(Long roleId) {
        return xyRoleFinder.selectById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(XyRole role) {
        boolean exist = xyRoleFinder.checkRoleNameUnique(role);
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(XyRole role) {
        boolean exist = xyRoleFinder.checkRoleKeyUnique(role);
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(XyRole role) {
        if (ObjectUtil.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }



    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public long countUserRoleByRoleId(Long roleId) {
        return xyUserRoleFinder.selectCount(roleId);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(XyRole role) {
        // 新增角色信息
        xyRoleFinder.insert(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(XyRole role) {
        // 修改角色信息
        xyRoleFinder.updateById(role);
        // 删除角色与菜单关联
        xyRoleMenuFinder.delete(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(XyRole role) {
        return xyRoleFinder.updateById(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(XyRole role) {
        // 修改角色信息
        return xyRoleFinder.updateById(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(XyRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<XyRoleMenu> list = new ArrayList<XyRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            XyRoleMenu rm = new XyRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = xyRoleMenuFinder.insertBatch(list) ? list.size() : 0;
        }
        return rows;
    }



    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        xyRoleMenuFinder.delete(roleId);
        return xyRoleFinder.deleteById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new XyRole(roleId));
            XyRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        List<Long> ids = Arrays.asList(roleIds);
        // 删除角色与菜单关联
        xyRoleMenuFinder.deleteRoleByIds(ids);
        return xyRoleFinder.deleteBatchIds(ids);
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(XyUserRole userRole) {
        return xyUserRoleFinder.deleteAuthUser(userRole);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return xyUserRoleFinder.deleteAuthUsers(roleId,userIds);
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        int rows = 1;
        List<XyUserRole> list = new ArrayList<XyUserRole>();
        for (Long userId : userIds) {
            XyUserRole ur = new XyUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            rows = xyUserRoleFinder.insertBatch(list) ? list.size() : 0;
        }
        return rows;
    }
}
