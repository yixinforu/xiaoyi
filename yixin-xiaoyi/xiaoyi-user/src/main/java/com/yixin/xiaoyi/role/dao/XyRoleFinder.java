package com.yixin.xiaoyi.role.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.role.entity.XyRole;
import com.yixin.xiaoyi.role.dao.mapper.XyRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/20 17:12
 */
@Repository
@RequiredArgsConstructor
public class XyRoleFinder {
    private final XyRoleMapper xyMenuMapper;

    public XyRole selectById(Long roleId){
        return xyMenuMapper.selectById(roleId);
    }

    public Page<XyRole> selectPageRoleList(XyRole role, PageQuery pageQuery){
        return xyMenuMapper.selectPageRoleList(pageQuery.build(), this.buildQueryWrapper(role));
    }

    public List<XyRole> selectRoleList(XyRole role){
        return xyMenuMapper.selectRoleList(this.buildQueryWrapper(role));
    }

    /**
     * 获取未过期的角色
     * @param userId
     * @return
     */
    public List<XyRole> selectRolePermissionByUserId(Long userId){
        return xyMenuMapper.selectRolePermissionByUserId(userId);
    }

    public List<Long> selectRoleListByUserId(Long userId) {
        return xyMenuMapper.selectRoleListByUserId(userId);
    }

    public XyRole selectRoleById(Long roleId) {
        return xyMenuMapper.selectById(roleId);
    }

    public boolean checkRoleNameUnique(XyRole role){
        return xyMenuMapper.exists(new LambdaQueryWrapper<XyRole>()
            .eq(XyRole::getRoleName, role.getRoleName())
            .ne(ObjectUtil.isNotNull(role.getRoleId()), XyRole::getRoleId, role.getRoleId()));
    }

    public boolean checkRoleKeyUnique(XyRole role){
        return xyMenuMapper.exists(new LambdaQueryWrapper<XyRole>()
            .eq(XyRole::getRoleKey, role.getRoleKey())
            .ne(ObjectUtil.isNotNull(role.getRoleId()), XyRole::getRoleId, role.getRoleId()));
    }

    public int insert(XyRole role){
        return xyMenuMapper.insert(role);
    }

    public int updateById(XyRole role){
        return  xyMenuMapper.updateById(role);
    }

    public int deleteById(Long roleId){
        return xyMenuMapper.deleteById(roleId);
    }

    public int deleteBatchIds(List<Long> roleIds){
        return xyMenuMapper.deleteBatchIds(roleIds);
    }

    public List<XyRole> selectRolesByUserName(String userName){
        return xyMenuMapper.selectRolesByUserName(userName);
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
}
