package com.yixin.xiaoyi.client.user;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.module.user.vo.XyRoleVO;
import com.yixin.xiaoyi.role.entity.XyRole;
import com.yixin.xiaoyi.role.entity.XyUserRole;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.user.entity.XyUser;
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

/**
 * @author: huangzexin
 * @date: 2023/8/2 22:32
 */
@FeignClient(
    name = "xy-role",
    contextId = "xy-role-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyRoleClient {

    /**
     * 获取角色信息列表
     * @param xyRoleJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/role/list")
    public TableDataInfo<XyRole> list(@RequestParam("xyRoleJson") String xyRoleJson, @RequestParam("pageQueryJson") String pageQueryJson);


    /**
     *
     * 根据角色编号获取详细信息
     * @param roleId 角色ID
     * @return
     */
    @GetMapping(value = "/inner/role/{roleId}")
    public R<XyRole> getInfo(@PathVariable("roleId") Long roleId);

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping("/inner/role")
    public R<Void> add(@Validated @RequestBody XyRole role);

    /**
     * 修改保存角色
     * @param role
     * @return
     */
    @PutMapping("/inner/role")
    public R<Void> edit(@Validated @RequestBody XyRole role);

    /**
     * 修改保存数据权限
     * @param role
     * @return
     */
    @PutMapping("/inner/role/dataScope")
    public R<Void> dataScope(@RequestBody XyRole role);

    /**
     * 状态修改
     * @param role
     * @return
     */
    @PutMapping("/inner/role/changeStatus")
    public R<Void> changeStatus(@RequestBody XyRole role);

    /**
     *
     * 删除角色
     * @param roleIds 角色ID串
     * @return
     */
    @DeleteMapping("/inner/role/{roleIds}")
    public R<Void> remove(@PathVariable("roleIds") Long[] roleIds);

    /**
     * 获取角色选择框列表
     * @return
     */
    @GetMapping("/inner/role/optionselect")
    public R<List<XyRole>> optionselect();

    /**
     * 查询已分配用户角色列表
     * @param xyUserJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/role/authUser/allocatedList")
    public TableDataInfo<XyUser> allocatedList(@RequestParam("xyUserJson") String xyUserJson,@RequestParam("pageQueryJson") String pageQueryJson) ;


    /**
     * 查询未分配用户角色列表
     * @param xyUserJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/role/authUser/unallocatedList")
    public TableDataInfo<XyUser> unallocatedList(@RequestParam("xyUserJson") String xyUserJson, @RequestParam("pageQueryJson") String pageQueryJson);

    /**
     * 取消授权用户
     * @param systemRole
     * @return
     */
    @PutMapping("/inner/role/authUser/cancel")
    public R<Void> cancelAuthUser(@RequestBody XyUserRole systemRole);

    /**
     *
     * 批量取消授权用户
     * @param xyRole  角色ID
     * @return
     */
    @PutMapping("/inner/role/authUser/cancelAll")
    public R<Void> cancelAuthUserAll(@RequestBody XyRoleVO xyRole);

    /**
     *
     * 批量选择用户授权
     * @param xyRole  角色ID
     * @return
     */
    @PutMapping("/inner/role/authUser/selectAll")
    public R<Void> selectAuthUserAll(@RequestBody XyRoleVO xyRole);

}


