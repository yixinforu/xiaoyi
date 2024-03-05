package com.yixin.xiaoyi.common.core.module.common.dto;

import lombok.Data;

/**
 * @author: huangzexin
 * @date: 2023/9/3 11:48
 */
@Data
public class DataCountDTO {

    /**
     * 当前总量
     */
    private Integer currentCount;

    /**
     * 今日新增
     */
    private Integer todayAddCount;

    public DataCountDTO(Integer currentCount, Integer todayAddCount) {
        this.currentCount = currentCount;
        this.todayAddCount = todayAddCount;
    }

    public DataCountDTO() {}
}
