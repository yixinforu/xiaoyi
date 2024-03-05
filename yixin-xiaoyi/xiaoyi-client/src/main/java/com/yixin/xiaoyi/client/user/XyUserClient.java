package com.yixin.xiaoyi.client.user;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.core.module.user.vo.XyUserExportVo;
import com.yixin.xiaoyi.common.core.module.user.vo.XyUserVO;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.user.model.dto.UserDTO;
import com.yixin.xiaoyi.user.model.vo.DisableVo;
import com.yixin.xiaoyi.user.model.vo.EmpowerVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/2 22:34
 */
@FeignClient(
    name = "xy-user",
    contextId = "xy-user-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyUserClient {

    /**
     * 获取用户列表
     * @param xyUserInfoVO
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/user/list")
    public TableDataInfo<UserDTO>  list(@RequestParam("xyUserInfoVO") String xyUserInfoVO, @RequestParam("pageQueryJson") String pageQueryJson) ;


    /**
     * VIP权限授予
     * @param empowerVO
     * @return
     */
    @PutMapping("/inner/user/empower")
    public R<Void> empower(@Validated @RequestBody EmpowerVO empowerVO);


    /**
     *
     * 封禁用户
     * @param disableVo
     * @return
     */
    @PutMapping("/inner/user/disable")
    public R<Void> disableUser(@Validated @RequestBody DisableVo disableVo);


    /**
     * 解除封禁
     * @param userId
     * @return
     */
    @PutMapping("/inner/user/untieDisable/{userId}")
    public R<Void> untieDisableUser(@PathVariable("userId") Long userId);

    /**
     * 导出用户列表
     * @param xyUserJson
     * @return
     */
    @GetMapping("/inner/user/export_list")
    public List<XyUserExportVo> export(@RequestParam("xyUserJson") String xyUserJson);

    /**
     *
     * 导入数据
     * @param xyUserVO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/inner/user/importData")
    public R<Void> importData( XyUserVO xyUserVO) throws Exception ;

    /**
     *
     * 获取导入模板
     * @param response
     */
    @PostMapping("/inner/user/importTemplate")
    public void importTemplate(HttpServletResponse response);

    /**
     *
     * 根据用户编号获取详细信息
     * @param userId
     * @return
     */
    @GetMapping(value = { "/inner/user/{userId}"})
    public R<Map<String, Object>> getInfo(@PathVariable(value = "userId", required = false) Long userId) ;

    /**
     * 修改用户
     * @param user
     * @return
     */
    @PutMapping("/inner/user")
    public R<Void> edit(@Validated @RequestBody XyUser user);

    /**
     * 重置密码
     * @param user
     * @return
     */
    @PutMapping("/inner/user/resetPwd")
    public R<Void> resetPwd(@RequestBody XyUser user);

    /**
     * 状态修改
     * @param user
     * @return
     */
    @PutMapping("/inner/user/changeStatus")
    public R<Void> changeStatus(@RequestBody XyUser user);


    /**
     * 用户授权角色
     * @param xyUserVO
     * @return
     */
    @PutMapping("/inner/user/authRole")
    public R<Void> insertAuthRole(XyUserVO xyUserVO);
    }
