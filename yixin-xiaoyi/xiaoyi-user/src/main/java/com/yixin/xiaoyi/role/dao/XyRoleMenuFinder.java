package com.yixin.xiaoyi.role.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yixin.xiaoyi.role.entity.XyRoleMenu;
import com.yixin.xiaoyi.role.dao.mapper.XyRoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/20 17:13
 */
@Repository
@RequiredArgsConstructor
public class XyRoleMenuFinder {

    private final  XyRoleMenuMapper xyRoleMenuMapper;

    public boolean checkMenuExistRole(Long menuId){
        return xyRoleMenuMapper.exists(new LambdaQueryWrapper<XyRoleMenu>().eq(XyRoleMenu::getMenuId, menuId));
    }

    public int delete(Long roleId){
        return xyRoleMenuMapper.delete(new LambdaQueryWrapper<XyRoleMenu>().eq(XyRoleMenu::getRoleId, roleId));
    }

    public int deleteRoleByIds(List<Long> ids){
        return xyRoleMenuMapper.delete(new LambdaQueryWrapper<XyRoleMenu>().in(XyRoleMenu::getRoleId, ids));
    }

    public boolean insertBatch(List<XyRoleMenu> list){
        return xyRoleMenuMapper.insertBatch(list);
    }
}
