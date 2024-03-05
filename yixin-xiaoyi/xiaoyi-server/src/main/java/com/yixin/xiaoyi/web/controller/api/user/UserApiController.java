package com.yixin.xiaoyi.web.controller.api.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import com.yixin.xiaoyi.common.constant.Constants;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.domain.model.LoginBody;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.PhoneUtil;
import com.yixin.xiaoyi.role.service.XyMenuAdminService;
import com.yixin.xiaoyi.sms.model.vo.SmsVO;
import com.yixin.xiaoyi.user.model.dto.GiftDTO;
import com.yixin.xiaoyi.user.model.dto.UserInfoDTO;
import com.yixin.xiaoyi.user.service.SysLoginService;
import com.yixin.xiaoyi.user.service.XyUserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/10/10 20:27
 */
@Validated
@RestController
@RequestMapping("/c/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserApiController {

    private final SysLoginService loginService;
    private final XyUserApiService userService;
    @Value("${gift.url}")
    private String giftUrl;
    @Value("${gift.password}")
    private String giftPassword;




    /**
     * 发送短信验证码
     * @param smsVO
     * @return
     */
    @SaIgnore
    @PostMapping("/captcha/code")
    public R<String> sendSmsCode(@Validated @RequestBody SmsVO smsVO){
        //手机短信验证码格式校验
        if(!PhoneUtil.verifyPhone(smsVO.getPhone())){
            throw new ServiceException(ErrorCode.USER_PHONE_FAIL);
        }
        userService.sendSmsCode(smsVO);
        return R.ok();
    }

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @SaIgnore
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBody loginBody) {
        if(!PhoneUtil.verifyPhone(loginBody.getPhone())){
            throw new ServiceException(ErrorCode.USER_PHONE_FAIL);
        }
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.login(loginBody);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }


    /**
     * 退出登录
     */
    @SaIgnore
    @PostMapping("/logout")
    public R<Void> logout() {
        loginService.logout();
        return R.ok("退出成功");
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public R<UserInfoDTO> getInfo() {
        return R.ok(userService.getUserInfo());
    }


    /**
     * 获取礼包链接
     * @return
     */
    @SaCheckPermission(value = {"user:gift:query"})
    @GetMapping("/gift")
    public R<GiftDTO> getGift(){
        GiftDTO giftDTO = new GiftDTO();
        giftDTO.setGiftUrl(giftUrl);
        giftDTO.setGiftPassword(giftPassword);
        return R.ok("操作成功",giftDTO);
    }


}
