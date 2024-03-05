package com.yixin.xiaoyi.recruit.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author: huangzexin
 * @date: 2023/9/3 20:45
 */
public enum RecruitNatureEnum {

    CAMPUS_NATURE("校招","校招"),
    PRACTICE_NATURE("实习","实习");


    @EnumValue
    private  String value;

    private  String name;

    RecruitNatureEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RecruitNatureEnum getEnumByValue(Long value) {
        for (RecruitNatureEnum type : RecruitNatureEnum.values()) {
            if (value.equals(type.value)) {
                return type;
            }
        }
        return null;
    }
}
