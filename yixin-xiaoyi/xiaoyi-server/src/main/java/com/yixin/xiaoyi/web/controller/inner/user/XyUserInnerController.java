package com.yixin.xiaoyi.web.controller.inner.user;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.user.model.dto.UserDTO;
import com.yixin.xiaoyi.user.model.vo.DisableVo;
import com.yixin.xiaoyi.user.model.vo.EmpowerVO;
import com.yixin.xiaoyi.user.model.vo.XyUserInfoVO;
import com.yixin.xiaoyi.role.entity.XyRole;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.core.module.user.vo.XyUserVO;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.excel.ExcelResult;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.common.utils.StreamUtils;
import com.yixin.xiaoyi.common.utils.poi.ExcelUtil;
import com.yixin.xiaoyi.common.core.module.user.vo.XyUserExportVo;
import com.yixin.xiaoyi.common.core.module.user.vo.XyUserImportVo;
import com.yixin.xiaoyi.user.listener.XyUserImportListener;
import com.yixin.xiaoyi.role.service.XyRoleAdminService;
import com.yixin.xiaoyi.user.service.XyUserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/user")
public class XyUserInnerController extends BaseController {

    private final XyUserAdminService userService;
    private final XyRoleAdminService roleService;

    /**
     * 获取用户列表
     * @param xyUserInfoVO
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<UserDTO> list(@RequestParam("xyUserInfoVO") String xyUserInfoVO, @RequestParam("pageQueryJson") String pageQueryJson) {
        return userService.selectPageUserList(JsonUtils.parseObject(xyUserInfoVO, XyUserInfoVO.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }

    /**
     * 授权
     * @param empowerVO
     * @return
     */
    @PutMapping("/empower")
    public R<Void> empower(@Validated @RequestBody EmpowerVO empowerVO){
        return toAjax(userService.empower(empowerVO));
    }

    /**
     * 封禁用户
     * @param disableVo
     * @return
     */
    @PutMapping("/disable")
    public R<Void> disableUser(@Validated @RequestBody DisableVo disableVo){
        userService.disableUser(disableVo);
        return R.ok();
    }


    /**
     * 解除封禁
     * @param userId
     * @return
     */
    @PutMapping("/untieDisable/{userId}")
    public R<Void> untieDisableUser(@PathVariable("userId") Long userId){
        userService.untieDisableUser(userId);
        return R.ok();
    }

    /**
     * 导出用户列表
     */
    @GetMapping("/export_list")
    public List<XyUserExportVo> export(@RequestParam("xyUserJson") String xyUserJson) {
        List<XyUser> list = userService.selectUserList(JsonUtils.parseObject(xyUserJson,XyUser.class ));
        List<XyUserExportVo> listVo = BeanUtil.copyToList(list, XyUserExportVo.class);
        return listVo;
    }

    /**
     * 导入数据
     *
     * @param xyUserVO          导入文件
     */
    @PostMapping(value = "/importData")
    public R<Void> importData( XyUserVO xyUserVO) throws Exception {
        ExcelResult<XyUserImportVo> result = ExcelUtil.importExcel(xyUserVO.getFile().getInputStream(), XyUserImportVo.class, new XyUserImportListener(xyUserVO.getUpdateSupport()));
        return R.ok(result.getAnalysis());
    }

    /**
     * 获取导入模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "用户数据", XyUserImportVo.class, response);
    }


    /**
     * 根据用户编号获取详细信息
     *
     * @param userId 用户ID
     */
    @GetMapping(value = {"/", "/{userId}"})
    public R<Map<String, Object>> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        Map<String, Object> ajax = new HashMap<>();
        List<XyRole> roles = roleService.selectRoleAll();
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
        if (ObjectUtil.isNotNull(userId)) {
            XyUser XyUser = userService.selectUserById(userId);
            ajax.put("user", XyUser);
            ajax.put("roleIds", StreamUtils.toList(XyUser.getRoles(), XyRole::getRoleId));
        }
        return R.ok(ajax);
    }


    /**
     * 修改用户
     * @param user
     * @return
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody XyUser user) {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return R.fail("修改用户'" + user.getPhone() + "'失败，登录手机号已存在");
        }
        return toAjax(userService.updateUser(user));
    }


    /**
     * 状态修改
     * @param user
     * @return
     */
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody XyUser user) {
        userService.checkUserAllowed(user);
        return toAjax(userService.updateUserStatus(user));
    }



    /**
     * 用户授权角色
     *
     * @param xyUserVO  用户Id
     */
    @PutMapping("/authRole")
    public R<Void> insertAuthRole(XyUserVO xyUserVO) {
        userService.insertUserAuth(xyUserVO.getUserId(), xyUserVO.getRoleIds());
        return R.ok();
    }

}


