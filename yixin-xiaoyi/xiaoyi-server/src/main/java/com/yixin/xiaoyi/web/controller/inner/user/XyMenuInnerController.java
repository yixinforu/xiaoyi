package com.yixin.xiaoyi.web.controller.inner.user;

import cn.hutool.core.lang.tree.Tree;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.role.entity.XyMenu;
import com.yixin.xiaoyi.role.service.XyMenuAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/menu")
public class XyMenuInnerController extends BaseController {

    private final XyMenuAdminService menuService;

    /**
     * 获取菜单列表
     * @param xyMenuJson
     * @return
     */
    @GetMapping("/list")
    public R<List<XyMenu>> list(@RequestParam("xyMenuJson") String xyMenuJson) {
        List<XyMenu> menus = menuService.selectMenuList(JsonUtils.parseObject(xyMenuJson,XyMenu.class), 1L);
        return R.ok(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     *
     * @param menuId 菜单ID
     */
    @GetMapping(value = "/{menuId}")
    public R<XyMenu> getInfo(@PathVariable Long menuId) {
        return R.ok(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     * @param xyMenuJson
     * @return
     */
    @GetMapping("/treeselect")
    public R<List<Tree<Long>>> treeselect(@RequestParam("xyMenuJson") String xyMenuJson) {
        List<XyMenu> menus = menuService.selectMenuList(JsonUtils.parseObject(xyMenuJson,XyMenu.class), 1L);
        return R.ok(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     * @param roleId
     * @return
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public R<Map<String, Object>> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<XyMenu> menus = menuService.selectMenuList(1L);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return R.ok(ajax);
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody XyMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody XyMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public R<Void> remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return R.warn("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return R.warn("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}
