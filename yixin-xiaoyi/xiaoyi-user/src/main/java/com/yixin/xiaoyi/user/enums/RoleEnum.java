package com.yixin.xiaoyi.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yixin.xiaoyi.common.config.UserConfig;

/**
 * @author: yixin
 * @date: 2023/2/24 14:22
 */
public enum RoleEnum {

    ROLE_USER(UserConfig.getBaseRoleId(),"普通用户"),
    ROLE_VIP(UserConfig.getVipRoleId(),"VIP用户");


    @EnumValue
    private  Long value;

    private  String name;

    RoleEnum(Long value, String name) {
        this.value = value;
        this.name = name;
    }
    public Long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RoleEnum getEnumByValue(Long value) {
        for (RoleEnum type : RoleEnum.values()) {
            if (value.equals(type.value)) {
                return type;
            }
        }
        return null;
    }

}
