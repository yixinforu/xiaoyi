package com.yixin.xiaoyi.web.controller.inner.user;

import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.role.entity.XyRole;
import com.yixin.xiaoyi.role.entity.XyUserRole;
import com.yixin.xiaoyi.role.service.XyRoleAdminService;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.core.module.user.vo.XyRoleVO;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.user.service.XyUserAdminService;
import com.yixin.xiaoyi.user.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/role")
public class XyRoleInnerController extends BaseController {

    private final XyRoleAdminService roleService;
    private final XyUserAdminService userService;
    private final SysPermissionService permissionService;

    /**
     * 获取角色信息列表
     * @param xyRoleJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<XyRole> list(@RequestParam("xyRoleJson") String xyRoleJson, @RequestParam("pageQueryJson") String pageQueryJson) {
        return roleService.selectPageRoleList(JsonUtils.parseObject(xyRoleJson,XyRole.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }


    /**
     * 根据角色编号获取详细信息
     *
     * @param roleId 角色ID
     */
    @GetMapping(value = "/{roleId}")
    public R<XyRole> getInfo(@PathVariable Long roleId) {
        return R.ok(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody XyRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     * @param role
     * @return
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody XyRole role) {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }

        if (roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            RedisUtils.deleteKeys(CacheConstants.PERMISSION+ "*");
            return R.ok();
        }
        return R.fail("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     * @param role
     * @return
     */
    @PutMapping("/dataScope")
    public R<Void> dataScope(@RequestBody XyRole role) {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     * @param role
     * @return
     */
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody XyRole role) {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色ID串
     */
    @DeleteMapping("/{roleIds}")
    public R<Void> remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     * @return
     */
    @GetMapping("/optionselect")
    public R<List<XyRole>> optionselect() {
        return R.ok(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     * @param xyUserJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo<XyUser> allocatedList(@RequestParam("xyUserJson") String xyUserJson,@RequestParam("pageQueryJson") String pageQueryJson) {
        return userService.selectAllocatedList(JsonUtils.parseObject(xyUserJson,XyUser.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }

    /**
     * 查询未分配用户角色列表
     * @param xyUserJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo<XyUser> unallocatedList(@RequestParam("xyUserJson") String xyUserJson,@RequestParam("pageQueryJson") String pageQueryJson) {
        return userService.selectUnallocatedList(JsonUtils.parseObject(xyUserJson,XyUser.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }

    /**
     * 取消授权用户
     * @param userRole
     * @return
     */
    @PutMapping("/authUser/cancel")
    public R<Void> cancelAuthUser(@RequestBody XyUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     *
     * @param xyRole  角色ID
     */
    @PutMapping("/authUser/cancelAll")
    public R<Void> cancelAuthUserAll(@RequestBody XyRoleVO xyRole) {
        return toAjax(roleService.deleteAuthUsers(xyRole.getRoleId(), xyRole.getUserIds()));
    }

    /**
     * 批量选择用户授权
     *
     * @param xyRoleVO  角色ID
     */
    @PutMapping("/authUser/selectAll")
    public R<Void> selectAuthUserAll(@RequestBody XyRoleVO xyRoleVO) {
        return toAjax(roleService.insertAuthUsers(xyRoleVO.getRoleId(), xyRoleVO.getUserIds()));
    }

}
