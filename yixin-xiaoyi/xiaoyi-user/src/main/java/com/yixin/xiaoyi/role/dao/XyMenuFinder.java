package com.yixin.xiaoyi.role.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.role.entity.XyMenu;
import com.yixin.xiaoyi.role.dao.mapper.XyMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/20 17:12
 */
@Repository
@RequiredArgsConstructor
public class XyMenuFinder {

    private final XyMenuMapper xyMenuMapper;

    public List<XyMenu> selectList(XyMenu menu){
        return xyMenuMapper.selectList(new LambdaQueryWrapper<XyMenu>()
            .like(StringUtils.isNotBlank(menu.getMenuName()), XyMenu::getMenuName, menu.getMenuName())
            .eq(StringUtils.isNotBlank(menu.getVisible()), XyMenu::getVisible, menu.getVisible())
            .eq(StringUtils.isNotBlank(menu.getStatus()), XyMenu::getStatus, menu.getStatus())
            .orderByAsc(XyMenu::getParentId)
            .orderByAsc(XyMenu::getOrderNum));
    }

    public List<XyMenu>  selectMenuListByUserId(XyMenu menu, Long userId){
        QueryWrapper<XyMenu> wrapper = Wrappers.query();
        wrapper.eq("sur.user_id", userId)
            .like(StringUtils.isNotBlank(menu.getMenuName()), "m.menu_name", menu.getMenuName())
            .eq(StringUtils.isNotBlank(menu.getVisible()), "m.visible", menu.getVisible())
            .eq(StringUtils.isNotBlank(menu.getStatus()), "m.status", menu.getStatus())
            .orderByAsc("m.parent_id")
            .orderByAsc("m.order_num");
        return xyMenuMapper.selectMenuListByUserId(wrapper);
    }

    public List<String> selectMenuPermsByUserId(Long userId){
        return xyMenuMapper.selectMenuPermsByUserId(userId);
    }

    public List<String> selectMenuPermsByRoleId(Long roleId){
        return xyMenuMapper.selectMenuPermsByRoleId(roleId);
    }

    public List<XyMenu> selectMenuPermsByRoles(List<Long> roleIds){
        return xyMenuMapper.selectMenuPermsByRoles(roleIds);
    }

    public List<XyMenu> selectMenuTreeAll(){
        return xyMenuMapper.selectMenuTreeAll();
    }

    public List<XyMenu> selectMenuTreeByUserId(Long userId){
        return xyMenuMapper.selectMenuTreeByUserId(userId);
    }

    public List<Long> selectMenuListByRoleId(Long roleId,boolean menuCheckStrictly){
        return xyMenuMapper.selectMenuListByRoleId(roleId, menuCheckStrictly);
    }

    public XyMenu selectById(Long menuId){
        return xyMenuMapper.selectById(menuId);
    }

    public boolean hasChildByMenuId(Long menuId){
        return xyMenuMapper.exists(new LambdaQueryWrapper<XyMenu>().eq(XyMenu::getParentId, menuId));
    }

    public int insert(XyMenu menu){
        return xyMenuMapper.insert(menu);
    }

    public int updateById(XyMenu menu){
        return xyMenuMapper.updateById(menu);
    }

    public int deleteById(Long menuId){
        return xyMenuMapper.deleteById(menuId);
    }

    public boolean checkMenuNameUnique(XyMenu menu){
        return xyMenuMapper.exists(new LambdaQueryWrapper<XyMenu>()
            .eq(XyMenu::getMenuName, menu.getMenuName())
            .eq(XyMenu::getParentId, menu.getParentId())
            .ne(ObjectUtil.isNotNull(menu.getMenuId()), XyMenu::getMenuId, menu.getMenuId()));
    }
}
