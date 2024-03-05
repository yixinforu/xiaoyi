package com.yixin.xiaoyi.common.core.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 小程序登录用户身份权限
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class XcxLoginUser extends LoginUser {

    private static final long serialVersionUID = 1L;

    /**
     * openid
     */
    private String openid;

}
