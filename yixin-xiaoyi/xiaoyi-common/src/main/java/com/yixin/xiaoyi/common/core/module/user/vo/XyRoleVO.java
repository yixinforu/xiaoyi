package com.yixin.xiaoyi.common.core.module.user.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: huangzexin
 * @date: 2023/8/3 22:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class XyRoleVO {

    private Long roleId;

    private  Long[] userIds;
}
