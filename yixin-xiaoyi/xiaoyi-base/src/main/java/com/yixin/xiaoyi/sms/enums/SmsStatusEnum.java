package com.yixin.xiaoyi.sms.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author: huangzexin
 * @date: 2023/10/8 21:21
 */
public enum SmsStatusEnum {

    SMS_OK("OK","发送成功");


    @EnumValue
    private  String value;

    private  String name;

    SmsStatusEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static SmsStatusEnum getEnumByValue(String value) {
        for (SmsStatusEnum type : SmsStatusEnum.values()) {
            if (value.equals(type.value)) {
                return type;
            }
        }
        return null;
    }
}
