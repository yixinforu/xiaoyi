package com.yixin.xiaoyi.role.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.role.entity.XyMenu;
import com.yixin.xiaoyi.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author admin
 */
public interface XyMenuMapper extends BaseMapperPlus<XyMenuMapper, XyMenu, XyMenu> {

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    List<String> selectMenuPerms();


    /**
     * 根据用户查询系统菜单列表
     *
     * @param queryWrapper 查询条件
     * @return 菜单列表
     */
    List<XyMenu> selectMenuListByUserId(@Param(Constants.WRAPPER) Wrapper<XyMenu> queryWrapper);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 获取角色对应的权限
     * @param roleIds
     * @return
     */
    List<XyMenu> selectMenuPermsByRoles(@Param("roleIds")List<Long> roleIds);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    default List<XyMenu> selectMenuTreeAll() {
        LambdaQueryWrapper<XyMenu> lqw = new LambdaQueryWrapper<XyMenu>()
            .in(XyMenu::getMenuType, UserConstants.TYPE_DIR, UserConstants.TYPE_MENU)
            .eq(XyMenu::getStatus, UserConstants.MENU_NORMAL)
            .orderByAsc(XyMenu::getParentId)
            .orderByAsc(XyMenu::getOrderNum);
        return this.selectList(lqw);
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<XyMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);



}
