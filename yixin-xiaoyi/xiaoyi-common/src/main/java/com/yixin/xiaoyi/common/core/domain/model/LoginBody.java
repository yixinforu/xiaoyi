package com.yixin.xiaoyi.common.core.domain.model;

import com.yixin.xiaoyi.common.constant.UserConstants;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 *
 * @author admin
 */

@Data
public class LoginBody {

    /**
     * 用户名
     */
    @NotBlank(message = "手机号不允许为空")
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "{验证码不允许为空}")
    private String smsCode;


    /**
     * 邀请码
     */
    private String inviteCode;


}
