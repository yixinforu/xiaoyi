package com.yixin.xiaoyi.client.user;

import cn.hutool.core.lang.tree.Tree;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.role.entity.XyMenu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/2 22:31
 */
@FeignClient(
    name = "xy-menu",
    contextId = "xy-menu-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface  XyMenuClient {

    /**
     * 获取菜单列表
     * @param xyMenuJson
     * @return
     */
    @GetMapping("/inner/menu/list")
    public R<List<XyMenu>> list(@RequestParam("xyMenuJson") String xyMenuJson) ;

    /**
     * 根据菜单编号获取详细信息
     * @param menuId
     * @return
     */
    @GetMapping(value = "/inner/menu/{menuId}")
    public R<XyMenu> getInfo(@PathVariable("menuId") Long menuId);

    /**
     * 获取菜单下拉树列表
     * @param xyMenuJson
     * @return
     */
    @GetMapping("/inner/menu/treeselect")
    public R<List<Tree<Long>>> treeselect(@RequestParam("xyMenuJson") String xyMenuJson);

    /**
     * 加载对应角色菜单列表树
     * @param roleId 角色id
     * @return
     */
    @GetMapping(value = "/inner/menu/roleMenuTreeselect/{roleId}")
    public R<Map<String, Object>> roleMenuTreeselect(@PathVariable("roleId") Long roleId);

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @PostMapping("/inner/menu")
    public R<Void> add(@Validated @RequestBody XyMenu menu);

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @PutMapping("/inner/menu")
    public R<Void> edit(@Validated @RequestBody XyMenu menu);

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @DeleteMapping("/inner/menu/{menuId}")
    public R<Void> remove(@PathVariable("menuId") Long menuId);

}
