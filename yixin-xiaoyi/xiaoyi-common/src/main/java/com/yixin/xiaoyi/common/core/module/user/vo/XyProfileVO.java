package com.yixin.xiaoyi.common.core.module.user.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: huangzexin
 * @date: 2023/8/3 22:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class XyProfileVO {


    private String oldPassword;

    private String newPassword;
}
